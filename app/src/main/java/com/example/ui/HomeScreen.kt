package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.*
import com.example.ui.theme.OrangeSecondary
import com.example.ui.theme.YellowOrangeDarkGradientBrush
import com.example.ui.theme.YellowPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val cartoons = CartoonRepository.sampleCartoons
    val iptvChannels by viewModel.iptvChannels.collectAsState()
    val torrents = CartoonRepository.sampleTorrents
    val spotifyTracks = CartoonRepository.sampleSpotifyTracks

    var showLangDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(YellowOrangeDarkGradientBrush),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // --- Header Banner & Logo ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cartoon_hero),
                    contentDescription = "Featured Cartoon Hero Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                // Top Bar overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "YellowCartoon Mascot",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(2.dp, YellowPrimary, CircleShape)
                        )
                        Column {
                            Text(
                                text = "YELLOWCARTOON",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = YellowPrimary
                            )
                            Text(
                                text = "PREMIUM TV",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeSecondary
                            )
                        }
                    }

                    // Language Selector Badge
                    Surface(
                        onClick = { showLangDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, YellowPrimary),
                        modifier = Modifier.testTag("btn_language")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = currentLang.flagEmoji, fontSize = 14.sp)
                            Text(
                                text = currentLang.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select Language",
                                tint = YellowPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Hero Details Overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = YellowPrimary
                    ) {
                        Text(
                            text = "XUPER TV FEATURED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = cartoons.first().title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.playCartoon(cartoons.first()) },
                            colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_hero_play")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (currentLang) {
                                    AppLanguage.SPANISH -> "VER AHORA"
                                    AppLanguage.PORTUGUESE -> "ASSISTIR AGORA"
                                    else -> "WATCH NOW"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.navigateTo(NavScreen.AI_CODEX) },
                            border = androidx.compose.foundation.BorderStroke(1.dp, YellowPrimary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_ask_codex")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "AI Codex", tint = YellowPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Codex")
                        }
                    }
                }
            }
        }

        // --- Quick Category Strip ---
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(MediaCategory.values()) { category ->
                    CategoryPill(
                        category = category,
                        currentLang = currentLang,
                        onClick = {
                            when (category) {
                                MediaCategory.LIVE_IPTV -> viewModel.navigateTo(NavScreen.IPTV)
                                MediaCategory.TORRENT_P2P -> viewModel.navigateTo(NavScreen.TORRENT)
                                MediaCategory.DVD_VLC -> viewModel.navigateTo(NavScreen.DVD)
                                MediaCategory.SORA_AI -> viewModel.navigateTo(NavScreen.HOME)
                                MediaCategory.SPOTIFY_OST -> viewModel.navigateTo(NavScreen.SPOTIFY)
                                else -> viewModel.navigateTo(NavScreen.HOME)
                            }
                        }
                    )
                }
            }
        }

        // --- Section 1: KissCartoon & Anime Collection ---
        item {
            SectionTitle(
                title = when (currentLang) {
                    AppLanguage.SPANISH -> "📺 Cartoons & Vault de Anime"
                    AppLanguage.PORTUGUESE -> "📺 Desenhos & Vault de Anime"
                    else -> "📺 Cartoons & Anime Vault"
                },
                subtitle = "KissCartoon, YouTube Kids & TMDB Highlights"
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartoons) { cartoon ->
                    CartoonCard(
                        cartoon = cartoon,
                        onClick = { viewModel.playCartoon(cartoon) }
                    )
                }
            }
        }

        // --- Section 2: Live IPTV Stream Preview ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(
                title = when (currentLang) {
                    AppLanguage.SPANISH -> "📡 Canales IPTV En Vivo (DangoPlayer)"
                    AppLanguage.PORTUGUESE -> "📡 Canais IPTV Ao Vivo"
                    else -> "📡 Live IPTV Channels (DangoPlayer)"
                },
                subtitle = "M3U Playlists: iptv-org, telechancho & tdtchannels"
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(iptvChannels.take(8)) { channel ->
                    IptvChannelCard(
                        channel = channel,
                        onClick = { viewModel.playIptvChannel(channel) }
                    )
                }
            }
        }

        // --- Section 3: WebTorrent & P2P Stream Hub ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(
                title = "⚡ WebTorrent P2P Stream Index",
                subtitle = "High-Speed Seeders for Cartoon HD Rips"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                torrents.forEach { torrent ->
                    TorrentRowCard(
                        torrent = torrent,
                        onStream = {
                            viewModel.navigateTo(NavScreen.TORRENT)
                        }
                    )
                }
            }
        }

        // --- Section 4: Spotify Cartoon Soundtracks ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(
                title = "🎵 Cartoon BSO & OSTs (Spotify)",
                subtitle = "Official theme songs & sound effects"
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(spotifyTracks) { track ->
                    SpotifyTrackCard(
                        track = track,
                        onClick = { viewModel.navigateTo(NavScreen.SPOTIFY) }
                    )
                }
            }
        }
    }

    // Language Selector Dialog
    if (showLangDialog) {
        AlertDialog(
            onDismissRequest = { showLangDialog = false },
            title = { Text("Select App Language / Idioma", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppLanguage.values().forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (currentLang == lang) YellowPrimary.copy(alpha = 0.2f) else Color.Transparent)
                                .clickable {
                                    viewModel.setLanguage(lang)
                                    showLangDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = lang.flagEmoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = lang.displayName, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLangDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun CategoryPill(
    category: MediaCategory,
    currentLang: AppLanguage,
    onClick: () -> Unit
) {
    val title = when (currentLang) {
        AppLanguage.SPANISH -> category.titleEs
        AppLanguage.PORTUGUESE -> category.titlePt
        else -> category.titleEn
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, YellowPrimary.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = when (category) {
                    MediaCategory.LIVE_IPTV -> Icons.Default.Tv
                    MediaCategory.TORRENT_P2P -> Icons.Default.Download
                    MediaCategory.DVD_VLC -> Icons.Default.DiscFull
                    MediaCategory.SORA_AI -> Icons.Default.AutoAwesome
                    MediaCategory.SPOTIFY_OST -> Icons.Default.MusicNote
                    else -> Icons.Default.Movie
                },
                contentDescription = title,
                tint = YellowPrimary,
                modifier = Modifier.size(16.dp)
            )
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CartoonCard(
    cartoon: CartoonItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .testTag("cartoon_card_${cartoon.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = cartoon.posterUrl,
                    contentDescription = cartoon.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = cartoon.rating,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = YellowPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = cartoon.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = cartoon.source,
                    fontSize = 11.sp,
                    color = OrangeSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun IptvChannelCard(
    channel: IptvChannel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = channel.logoUrl,
                contentDescription = channel.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = channel.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = channel.group,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TorrentRowCard(
    torrent: TorrentStream,
    onStream: () -> Unit
) {
    Surface(
        onClick = onStream,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, OrangeSecondary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.Download, contentDescription = null, tint = YellowPrimary)
                Column {
                    Text(text = torrent.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(text = "${torrent.quality} • ${torrent.size} • Seeds: ${torrent.seeders}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            IconButton(onClick = onStream) {
                Icon(Icons.Default.PlayCircle, contentDescription = "Stream Torrent", tint = YellowPrimary)
            }
        }
    }
}

@Composable
fun SpotifyTrackCard(
    track: SpotifyTrack,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = track.albumArt,
                contentDescription = track.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = track.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = track.artist, fontSize = 10.sp, color = YellowPrimary, maxLines = 1)
        }
    }
}
