package com.example.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.data.CartoonRepository
import com.example.data.SpotifyTrack
import com.example.ui.theme.YellowOrangeDarkGradientBrush
import com.example.ui.theme.YellowPrimary

@Composable
fun SpotifyScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val tracks = CartoonRepository.sampleSpotifyTracks
    var selectedTrack by remember { mutableStateOf(tracks.first()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YellowOrangeDarkGradientBrush)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(28.dp))
            Column {
                Text(
                    text = "🎵 Cartoon Spotify BSO & Soundtracks",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YellowPrimary
                )
                Text(
                    text = "Stream theme music directly via open.spotify.com",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Embedded Web Player Box for Spotify
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        loadUrl(selectedTrack.embedUrl)
                    }
                },
                update = { webView ->
                    webView.loadUrl(selectedTrack.embedUrl)
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("POPULAR CARTOON SOUNDTRACKS", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(tracks) { track ->
                Surface(
                    onClick = { selectedTrack = track },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = if (selectedTrack.id == track.id) androidx.compose.foundation.BorderStroke(1.5.dp, YellowPrimary) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = track.albumArt,
                            contentDescription = track.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = track.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = track.artist, fontSize = 11.sp, color = YellowPrimary)
                        }
                        IconButton(onClick = { selectedTrack = track }) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play Track", tint = YellowPrimary)
                        }
                    }
                }
            }
        }
    }
}
