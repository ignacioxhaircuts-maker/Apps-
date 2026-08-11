package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val mediaType: String, // "CARTOON", "IPTV", "TORRENT", "DVD", "YOUTUBE"
    val streamUrl: String,
    val posterUrl: String,
    val lastPositionMs: Long,
    val durationMs: Long,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String, // unique media ID or URL
    val title: String,
    val mediaType: String,
    val posterUrl: String,
    val rating: String,
    val genre: String,
    val isFavorite: Boolean = true,
    val dateAdded: Long = System.currentTimeMillis()
)

@Entity(tableName = "iptv_channels")
data class IptvChannelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val logoUrl: String,
    val groupName: String,
    val streamUrl: String,
    val isFavorite: Boolean = false
)

@Entity(tableName = "torrents")
data class TorrentEntity(
    @PrimaryKey val magnetUrl: String,
    val name: String,
    val category: String,
    val sizeText: String,
    val seeders: Int,
    val leechers: Int,
    val dateAdded: Long = System.currentTimeMillis()
)
