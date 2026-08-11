package com.example.data

enum class AppLanguage(val code: String, val displayName: String, val flagEmoji: String) {
    ENGLISH("en", "English", "🇺🇸"),
    SPANISH("es", "Español", "🇪🇸"),
    PORTUGUESE("pt", "Português", "🇧🇷")
}

enum class MediaCategory(val id: String, val titleEn: String, val titleEs: String, val titlePt: String) {
    CARTOONS("cartoons", "Cartoons & Classics", "Dibujos Animados", "Desenhos Animados"),
    ANIME("anime", "Anime Vault", "Bóveda de Anime", "Cofre de Anime"),
    LIVE_IPTV("iptv", "Live IPTV & Kids TV", "TV e IPTV En Vivo", "IPTV Ao Vivo"),
    TORRENT_P2P("p2p", "P2P Torrent Hub", "Torrents P2P", "Torrents P2P"),
    DVD_VLC("dvd", "DVD & VLC Player", "Reproductor DVD/VLC", "Reprodutor DVD/VLC"),
    SORA_AI("sora", "Sora 2 AI Showcase", "Colección Sora 2 AI", "Coleção Sora 2 AI"),
    SPOTIFY_OST("spotify", "Cartoon OSTs (Spotify)", "Música y BSO Cartoon", "Trilhas Sonoras (Spotify)")
}

data class CartoonItem(
    val id: String,
    val title: String,
    val genre: String,
    val rating: String, // e.g. "9.8 IMDB"
    val year: String,
    val language: String, // "en", "es", "pt"
    val posterUrl: String,
    val bannerUrl: String = posterUrl,
    val videoUrl: String,
    val description: String,
    val source: String = "YellowCartoon", // "KissCartoon", "YouTube Kids", "BiliBili", "Sora AI", "TMDB"
    val isFeatured: Boolean = false,
    val episodes: List<EpisodeItem> = emptyList()
)

data class EpisodeItem(
    val episodeNumber: Int,
    val title: String,
    val duration: String,
    val videoUrl: String
)

data class IptvChannel(
    val id: String,
    val name: String,
    val logoUrl: String,
    val group: String,
    val streamUrl: String,
    val country: String = "Global"
)

data class TorrentStream(
    val id: String,
    val title: String,
    val magnetUrl: String,
    val category: String,
    val quality: String,
    val size: String,
    val seeders: Int,
    val leechers: Int
)

data class SpotifyTrack(
    val id: String,
    val title: String,
    val artist: String,
    val albumArt: String,
    val embedUrl: String,
    val duration: String
)
