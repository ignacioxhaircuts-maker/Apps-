package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartoonRepository
import com.example.data.TorrentStream
import com.example.ui.theme.OrangeSecondary
import com.example.ui.theme.YellowOrangeDarkGradientBrush
import com.example.ui.theme.YellowPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TorrentScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var magnetInput by remember { mutableStateOf("") }
    val torrents = CartoonRepository.sampleTorrents

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YellowOrangeDarkGradientBrush)
            .padding(16.dp)
    ) {
        Text(
            text = "⚡ P2P & WebTorrent Player",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = YellowPrimary
        )
        Text(
            text = "Stream high-speed magnet links directly without waiting for downloads",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Magnet Link Input
        OutlinedTextField(
            value = magnetInput,
            onValueChange = { magnetInput = it },
            placeholder = { Text("Paste magnet link (magnet:?xt=urn:btih:...)") },
            leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = YellowPrimary) },
            trailingIcon = {
                Button(
                    onClick = {
                        if (magnetInput.isNotEmpty()) {
                            viewModel.playCartoon(
                                com.example.data.CartoonItem(
                                    id = "tor_${System.currentTimeMillis()}",
                                    title = "Custom Magnet P2P Stream",
                                    genre = "P2P Torrent",
                                    rating = "1080p",
                                    year = "2026",
                                    language = "en",
                                    posterUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&q=80",
                                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                    description = "P2P WebTorrent Stream active via magnet link."
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = androidx.compose.ui.graphics.Color.Black),
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .testTag("btn_start_torrent_stream")
                ) {
                    Text("STREAM", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "FEATURED P2P TORRENT STREAMS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(torrents) { torrent ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = torrent.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = YellowPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = torrent.quality,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YellowPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(text = "📦 ${torrent.size}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "🟢 Seeds: ${torrent.seeders}", fontSize = 11.sp, color = YellowPrimary)
                                Text(text = "🔴 Leeches: ${torrent.leechers}", fontSize = 11.sp, color = OrangeSecondary)
                            }

                            Button(
                                onClick = {
                                    viewModel.playCartoon(
                                        com.example.data.CartoonItem(
                                            id = torrent.id,
                                            title = torrent.title,
                                            genre = torrent.category,
                                            rating = torrent.quality,
                                            year = "2026",
                                            language = "en",
                                            posterUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&q=80",
                                            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                            description = "P2P WebTorrent stream seeded by ${torrent.seeders} nodes."
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = androidx.compose.ui.graphics.Color.Black),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Play Stream", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
