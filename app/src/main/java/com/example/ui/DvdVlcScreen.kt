package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.YellowOrangeDarkGradientBrush
import com.example.ui.theme.YellowPrimary

@Composable
fun DvdVlcScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var audioTrack by remember { mutableStateOf("English AC3 5.1") }
    var subtitleTrack by remember { mutableStateOf("English [SubRip]") }
    var aspectRatioMode by remember { mutableStateOf("16:9 Wide") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YellowOrangeDarkGradientBrush)
            .padding(16.dp)
    ) {
        Text(
            text = "💿 DVD & VLC Media Engine",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = YellowPrimary
        )
        Text(
            text = "ISO / VOB / Local Video File Player with Custom Audio & Subtitle Tracks",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Open File Action Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.DiscFull, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text("Open Local Video / DVD ISO File", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Supports .mkv, .mp4, .vob, .iso, .avi, .flv, .ts", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = {
                        viewModel.playCartoon(
                            com.example.data.CartoonItem(
                                id = "dvd_1",
                                title = "Local DVD ISO Movie (VLC Engine)",
                                genre = "Local Media",
                                rating = "1080p60",
                                year = "2026",
                                language = "en",
                                posterUrl = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=800&q=80",
                                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                                description = "Playing via libVLC Hardware Accelerated decoder."
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = androidx.compose.ui.graphics.Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.FolderOpen, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Browse & Play File", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("VLC DECODER CONTROLS", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Settings list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ControlRow(
                title = "Audio Track",
                value = audioTrack,
                onSelect = { audioTrack = if (audioTrack.contains("English")) "Spanish Audio (DUB)" else "English AC3 5.1" }
            )
            ControlRow(
                title = "Subtitles",
                value = subtitleTrack,
                onSelect = { subtitleTrack = if (subtitleTrack.contains("English")) "Español [Sub]" else "English [SubRip]" }
            )
            ControlRow(
                title = "Aspect Ratio",
                value = aspectRatioMode,
                onSelect = { aspectRatioMode = if (aspectRatioMode.contains("16:9")) "4:3 Classic TV" else "16:9 Wide" }
            )
        }
    }
}

@Composable
fun ControlRow(title: String, value: String, onSelect: () -> Unit) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = value, fontSize = 12.sp, color = YellowPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = YellowPrimary)
            }
        }
    }
}
