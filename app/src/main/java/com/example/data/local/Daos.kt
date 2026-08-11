package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM watch_history ORDER BY timestamp DESC")
    fun getWatchHistory(): Flow<List<WatchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchHistory(item: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE id = :id")
    suspend fun deleteWatchHistory(id: Long)

    @Query("DELETE FROM watch_history")
    suspend fun clearHistory()
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY dateAdded DESC")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeFavorite(id: String)
}

@Dao
interface IptvDao {
    @Query("SELECT * FROM iptv_channels ORDER BY name ASC")
    fun getAllChannels(): Flow<List<IptvChannelEntity>>

    @Query("SELECT * FROM iptv_channels WHERE groupName = :group ORDER BY name ASC")
    fun getChannelsByGroup(group: String): Flow<List<IptvChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(channels: List<IptvChannelEntity>)

    @Query("DELETE FROM iptv_channels")
    suspend fun clearChannels()
}

@Dao
interface TorrentDao {
    @Query("SELECT * FROM torrents ORDER BY dateAdded DESC")
    fun getTorrents(): Flow<List<TorrentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTorrent(torrent: TorrentEntity)

    @Query("DELETE FROM torrents WHERE magnetUrl = :magnetUrl")
    suspend fun deleteTorrent(magnetUrl: String)
}
