package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.YellowOrangeDarkGradientBrush
import com.example.ui.theme.YellowPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesHistoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val history by viewModel.watchHistory.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Favorites, 1 = History

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YellowOrangeDarkGradientBrush)
            .padding(16.dp)
    ) {
        Text(
            text = "⭐ Room Database Library",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = YellowPrimary
        )
        Text(
            text = "Saved Favorites & Watch History stored locally on device",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = YellowPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Favorites (${favorites.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Favorite, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Watch History (${history.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.History, contentDescription = null) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved favorites yet! Tap ⭐ on any cartoon or IPTV channel.", fontSize = 13.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    items(favorites) { fav ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AsyncImage(
                                    model = fav.posterUrl,
                                    contentDescription = fav.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = fav.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "${fav.mediaType} • ${fav.rating}", fontSize = 11.sp, color = YellowPrimary)
                                }
                                IconButton(onClick = {
                                    viewModel.playCartoon(
                                        com.example.data.CartoonItem(
                                            id = fav.id,
                                            title = fav.title,
                                            genre = fav.genre,
                                            rating = fav.rating,
                                            year = "2026",
                                            language = "en",
                                            posterUrl = fav.posterUrl,
                                            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                            description = "Playing from saved Room Favorites."
                                        )
                                    )
                                }) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = YellowPrimary)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (history.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your watch history is empty.", fontSize = 13.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    items(history) { item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AsyncImage(
                                    model = item.posterUrl,
                                    contentDescription = item.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "Watched ${item.mediaType}", fontSize = 11.sp, color = YellowPrimary)
                                }
                                IconButton(onClick = {
                                    viewModel.playCartoon(
                                        com.example.data.CartoonItem(
                                            id = "hist_${item.id}",
                                            title = item.title,
                                            genre = item.mediaType,
                                            rating = "HD",
                                            year = "2026",
                                            language = "en",
                                            posterUrl = item.posterUrl,
                                            videoUrl = item.streamUrl,
                                            description = "Resumed from watch history."
                                        )
                                    )
                                }) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Replay", tint = YellowPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
