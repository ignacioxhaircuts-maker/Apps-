package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiCartoonAssistant
import com.example.data.*
import com.example.data.local.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class NavScreen {
    HOME,
    PLAYER,
    IPTV,
    TORRENT,
    DVD,
    AI_CODEX,
    SPOTIFY,
    FAVORITES
}

data class AiMessage(
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val watchHistoryDao = db.watchHistoryDao()
    private val favoriteDao = db.favoriteDao()
    private val iptvDao = db.iptvDao()
    private val torrentDao = db.torrentDao()

    private val aiAssistant = GeminiCartoonAssistant()

    // UI States
    private val _currentScreen = MutableStateFlow(NavScreen.HOME)
    val currentScreen: StateFlow<NavScreen> = _currentScreen.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _selectedCartoon = MutableStateFlow<CartoonItem?>(CartoonRepository.sampleCartoons.first())
    val selectedCartoon: StateFlow<CartoonItem?> = _selectedCartoon.asStateFlow()

    private val _activeStreamUrl = MutableStateFlow("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
    val activeStreamUrl: StateFlow<String> = _activeStreamUrl.asStateFlow()

    private val _activeMediaTitle = MutableStateFlow("Yellow Mascot Adventures S1")
    val activeMediaTitle: StateFlow<String> = _activeMediaTitle.asStateFlow()

    // IPTV
    private val _iptvChannels = MutableStateFlow<List<IptvChannel>>(CartoonRepository.sampleIptvChannels)
    val iptvChannels: StateFlow<List<IptvChannel>> = _iptvChannels.asStateFlow()

    private val _isIptvLoading = MutableStateFlow(false)
    val isIptvLoading: StateFlow<Boolean> = _isIptvLoading.asStateFlow()

    private val _iptvSearchQuery = MutableStateFlow("")
    val iptvSearchQuery: StateFlow<String> = _iptvSearchQuery.asStateFlow()

    // AI Codex Chat
    private val _aiMessages = MutableStateFlow<List<AiMessage>>(
        listOf(
            AiMessage("AI", "👋 Hello! I am YellowCartoon Codex AI. Ask me anything about cartoons, live IPTV playlists, torrents, or retro KissCartoon episodes!")
        )
    )
    val aiMessages: StateFlow<List<AiMessage>> = _aiMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Room Flows
    val watchHistory: StateFlow<List<WatchHistoryEntity>> = watchHistoryDao.getWatchHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = favoriteDao.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadDefaultIptvPlaylists()
    }

    fun navigateTo(screen: NavScreen) {
        _currentScreen.value = screen
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun playCartoon(cartoon: CartoonItem) {
        _selectedCartoon.value = cartoon
        _activeStreamUrl.value = cartoon.videoUrl
        _activeMediaTitle.value = cartoon.title
        _currentScreen.value = NavScreen.PLAYER

        // Record in Room watch history
        viewModelScope.launch {
            watchHistoryDao.insertWatchHistory(
                WatchHistoryEntity(
                    title = cartoon.title,
                    mediaType = "CARTOON",
                    streamUrl = cartoon.videoUrl,
                    posterUrl = cartoon.posterUrl,
                    lastPositionMs = 0,
                    durationMs = 1200000
                )
            )
        }
    }

    fun playIptvChannel(channel: IptvChannel) {
        _activeStreamUrl.value = channel.streamUrl
        _activeMediaTitle.value = channel.name
        _currentScreen.value = NavScreen.PLAYER

        viewModelScope.launch {
            watchHistoryDao.insertWatchHistory(
                WatchHistoryEntity(
                    title = channel.name,
                    mediaType = "IPTV",
                    streamUrl = channel.streamUrl,
                    posterUrl = channel.logoUrl,
                    lastPositionMs = 0,
                    durationMs = 0
                )
            )
        }
    }

    fun toggleFavorite(cartoon: CartoonItem) {
        viewModelScope.launch {
            favoriteDao.insertFavorite(
                FavoriteEntity(
                    id = cartoon.id,
                    title = cartoon.title,
                    mediaType = "CARTOON",
                    posterUrl = cartoon.posterUrl,
                    rating = cartoon.rating,
                    genre = cartoon.genre
                )
            )
        }
    }

    fun updateIptvSearch(query: String) {
        _iptvSearchQuery.value = query
    }

    fun loadDefaultIptvPlaylists() {
        viewModelScope.launch {
            _isIptvLoading.value = true
            val parsedList = mutableListOf<IptvChannel>()
            parsedList.addAll(CartoonRepository.sampleIptvChannels)

            for (url in IptvParser.defaultM3uUrls.take(2)) {
                val fetched = IptvParser.fetchAndParseM3u(url)
                if (fetched.isNotEmpty()) {
                    parsedList.addAll(fetched.take(25))
                }
            }
            _iptvChannels.value = parsedList.distinctBy { it.streamUrl }
            _isIptvLoading.value = false
        }
    }

    fun sendAiPrompt(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = AiMessage("USER", prompt)
        _aiMessages.value = _aiMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val responseText = aiAssistant.askCodex(prompt, _currentLanguage.value.code)
            val aiMsg = AiMessage("AI", responseText)
            _aiMessages.value = _aiMessages.value + aiMsg
            _isAiThinking.value = false
        }
    }
}
