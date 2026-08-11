package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.theme.YellowCartoonTheme
import com.example.ui.theme.YellowPrimary

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YellowCartoonTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentScreen != NavScreen.PLAYER) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = YellowPrimary,
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("main_navigation_bar")
                            ) {
                                NavigationBarItem(
                                    selected = currentScreen == NavScreen.HOME,
                                    onClick = { viewModel.navigateTo(NavScreen.HOME) },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    modifier = Modifier.testTag("nav_item_home")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == NavScreen.IPTV,
                                    onClick = { viewModel.navigateTo(NavScreen.IPTV) },
                                    icon = { Icon(Icons.Default.Tv, contentDescription = "IPTV") },
                                    label = { Text("IPTV", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    modifier = Modifier.testTag("nav_item_iptv")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == NavScreen.TORRENT,
                                    onClick = { viewModel.navigateTo(NavScreen.TORRENT) },
                                    icon = { Icon(Icons.Default.Download, contentDescription = "P2P") },
                                    label = { Text("P2P Torrent", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    modifier = Modifier.testTag("nav_item_torrent")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == NavScreen.AI_CODEX,
                                    onClick = { viewModel.navigateTo(NavScreen.AI_CODEX) },
                                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Codex AI") },
                                    label = { Text("AI Codex", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    modifier = Modifier.testTag("nav_item_ai")
                                )

                                NavigationBarItem(
                                    selected = currentScreen == NavScreen.FAVORITES,
                                    onClick = { viewModel.navigateTo(NavScreen.FAVORITES) },
                                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Library") },
                                    label = { Text("Library", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    modifier = Modifier.testTag("nav_item_library")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                            when (screen) {
                                NavScreen.HOME -> HomeScreen(viewModel = viewModel)
                                NavScreen.PLAYER -> PlayerScreen(viewModel = viewModel)
                                NavScreen.IPTV -> IptvScreen(viewModel = viewModel)
                                NavScreen.TORRENT -> TorrentScreen(viewModel = viewModel)
                                NavScreen.DVD -> DvdVlcScreen(viewModel = viewModel)
                                NavScreen.AI_CODEX -> AiChatScreen(viewModel = viewModel)
                                NavScreen.SPOTIFY -> SpotifyScreen(viewModel = viewModel)
                                NavScreen.FAVORITES -> FavoritesHistoryScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
