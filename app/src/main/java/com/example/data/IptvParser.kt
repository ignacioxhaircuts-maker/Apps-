package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object IptvParser {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    val defaultM3uUrls = listOf(
        "https://iptv-org.github.io/iptv/index.m3u",
        "https://telechancho.github.io/infinity.m3u",
        "https://www.tdtchannels.com/lists/tv.m3u8",
        "https://raw.githubusercontent.com/dzh0ni/iPTV-FREE-LIST/master/iPTV-Free-List_TV.m3u",
        "https://raw.githubusercontent.com/Free-TV/IPTV/master/playlist.m3u8"
    )

    suspend fun fetchAndParseM3u(url: String): List<IptvChannel> = withContext(Dispatchers.IO) {
        val channels = mutableListOf<IptvChannel>()
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext emptyList()

            val bodyString = response.body?.string() ?: return@withContext emptyList()
            val lines = bodyString.lines()

            var currentName = ""
            var currentLogo = ""
            var currentGroup = "General"

            for (line in lines) {
                val trimmed = line.trim()
                if (trimmed.startsWith("#EXTINF:")) {
                    currentLogo = parseAttribute(trimmed, "tvg-logo")
                    val groupAttr = parseAttribute(trimmed, "group-title")
                    if (groupAttr.isNotEmpty()) {
                        currentGroup = groupAttr
                    }
                    val commaIndex = trimmed.lastIndexOf(',')
                    if (commaIndex != -1 && commaIndex < trimmed.length - 1) {
                        currentName = trimmed.substring(commaIndex + 1).trim()
                    } else {
                        currentName = "Channel ${channels.size + 1}"
                    }
                } else if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
                    if (currentName.isEmpty()) currentName = "Stream ${channels.size + 1}"
                    channels.add(
                        IptvChannel(
                            id = "iptv_${channels.size}_${trimmed.hashCode()}",
                            name = currentName,
                            logoUrl = currentLogo.ifEmpty { "https://picsum.photos/200/200?random=${channels.size}" },
                            group = currentGroup,
                            streamUrl = trimmed
                        )
                    )
                    currentName = ""
                    currentLogo = ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext channels
    }

    private fun parseAttribute(line: String, attrName: String): String {
        val pattern = "$attrName=\"([^\"]*)\"".toRegex(RegexOption.IGNORE_CASE)
        val match = pattern.find(line)
        return match?.groupValues?.getOrNull(1) ?: ""
    }
}
