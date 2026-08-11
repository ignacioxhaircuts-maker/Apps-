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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.YellowOrangeDarkGradientBrush
import com.example.ui.theme.YellowPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.aiMessages.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YellowOrangeDarkGradientBrush)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(28.dp))
            Column {
                Text(
                    text = "Gemini Codex AI Cartoon Assistant",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YellowPrimary
                )
                Text(
                    text = "Powered by Gemini 3.1 Pro Preview with High Thinking Mode",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat Message Log
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "USER"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isUser) YellowPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isUser) Color.Black else MaterialTheme.colorScheme.onSurface
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isUser) "You" else "Codex AI",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isUser) Color.DarkGray else YellowPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = msg.text, fontSize = 13.sp)
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = YellowPrimary, strokeWidth = 2.dp)
                        Text("Gemini AI is thinking deeply...", fontSize = 12.sp, color = YellowPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Prompt Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask about cartoons, IPTV, anime or KissCartoon...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_ai_prompt"),
                shape = RoundedCornerShape(12.dp)
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendAiPrompt(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .background(YellowPrimary, RoundedCornerShape(12.dp))
                    .testTag("btn_send_ai_prompt")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.Black)
            }
        }
    }
}
