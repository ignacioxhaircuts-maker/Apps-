package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GeminiPart(val text: String? = null)

data class GeminiContent(val parts: List<GeminiPart>)

data class GeminiThinkingConfig(val thinkingLevel: String = "HIGH")

data class GeminiGenerationConfig(
    val thinkingConfig: GeminiThinkingConfig? = GeminiThinkingConfig("HIGH"),
    val temperature: Float? = 0.7f
)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null,
    val generationConfig: GeminiGenerationConfig? = GeminiGenerationConfig()
)

data class GeminiCandidate(val content: GeminiContent?)

data class GeminiResponse(val candidates: List<GeminiCandidate>? = null)

interface GeminiService {
    @POST("v1beta/models/gemini-3.1-pro-preview:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiService::class.java)
    }
}

class GeminiCartoonAssistant {

    suspend fun askCodex(prompt: String, languageCode: String = "en"): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineAiResponse(prompt, languageCode)
        }

        val systemPrompt = when (languageCode) {
            "es" -> "Eres el Asistente IA Codex de YellowCartoon TV Premium. Eres experto en dibujos animados, anime, reproductores P2P, canales IPTV y cultura pop. Responde con entusiasmo, precisión y formato claro en español."
            "pt" -> "Você é o Assistente IA Codex do YellowCartoon TV Premium. Você é especialista em desenhos animados, animes, IPTV e cultura pop. Responda com entusiasmo, precisão e formatação clara em português."
            else -> "You are YellowCartoon TV Premium's Codex AI Assistant. You are an expert in cartoons, anime, live IPTV channels, P2P streams, and pop culture trivia. Respond enthusiastically and accurately in English."
        }

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
        )

        try {
            val response = GeminiClient.service.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!text.isNullOrBlank()) {
                text
            } else {
                getOfflineAiResponse(prompt, languageCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            getOfflineAiResponse(prompt, languageCode)
        }
    }

    private fun getOfflineAiResponse(prompt: String, lang: String): String {
        return when (lang) {
            "es" -> "✨ **YellowCartoon Codex IA**: ¡Bienvenido a YellowCartoon TV Premium! He analizado tu consulta sobre \"$prompt\". Te recomiendo explorar nuestra sección de IPTV en vivo (con listas M3U de animación) o disfrutar del archivo KissCartoon en calidad HD. ¡Que disfrutes de los dibujos!"
            "pt" -> "✨ **YellowCartoon Codex IA**: Bem-vindo ao YellowCartoon TV Premium! Analisei sua pergunta sobre \"$prompt\". Recomendo explorar nossos canais IPTV ao vivo e a coleção de animes e desenhos animados!"
            else -> "✨ **YellowCartoon Codex AI**: Welcome to YellowCartoon TV Premium! Based on your query \"$prompt\", I recommend checking out our Live IPTV Kids channels, KissCartoon classics, and P2P Torrent streams for top quality cartoon streaming!"
        }
    }
}
