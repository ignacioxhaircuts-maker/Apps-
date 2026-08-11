package com.example.ui

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.YellowPrimary

@Composable
fun PlayerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeStreamUrl by viewModel.activeStreamUrl.collectAsState()
    val activeMediaTitle by viewModel.activeMediaTitle.collectAsState()
    val cartoon by viewModel.selectedCartoon.collectAsState()

    var isCasting by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // --- Video Player Container ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        setVideoURI(Uri.parse(activeStreamUrl))
                        val mediaController = MediaController(context)
                        mediaController.setAnchorView(this)
                        setMediaController(mediaController)
                        start()
                    }
                },
                update = { videoView ->
                    videoView.setVideoURI(Uri.parse(activeStreamUrl))
                    videoView.start()
                },
                modifier = Modifier.fillMaxSize()
            )

            // Top overlay action bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(NavScreen.HOME) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Chromecast Button
                    IconButton(
                        onClick = { isCasting = !isCasting },
                        modifier = Modifier.testTag("btn_chromecast")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cast,
                            contentDescription = "Chromecast",
                            tint = if (isCasting) YellowPrimary else Color.White
                        )
                    }

                    // Favorite Button
                    cartoon?.let {
                        IconButton(onClick = { viewModel.toggleFavorite(it) }) {
                            Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = YellowPrimary)
                        }
                    }
                }
            }
        }

        if (isCasting) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = YellowPrimary
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.CastConnected, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Casting to Living Room TV...", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // --- Details & Episode List ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = activeMediaTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            cartoon?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${it.genre} • ${it.year} • ${it.rating}",
                    fontSize = 12.sp,
                    color = YellowPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it.description,
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "EPISODES / SEASONS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = YellowPrimary
            )

            cartoon?.episodes?.let { episodes ->
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(episodes) { ep ->
                        Surface(
                            onClick = {
                                viewModel.playCartoon(
                                    cartoon!!.copy(
                                        title = "${cartoon!!.title} - Ep ${ep.episodeNumber}",
                                        videoUrl = ep.videoUrl
                                    )
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E1E24)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Ep ${ep.episodeNumber}: ${ep.title}", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text(text = ep.duration, color = Color.Gray, fontSize = 11.sp)
                                }
                                Icon(Icons.Default.PlayCircle, contentDescription = "Play Episode", tint = YellowPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
