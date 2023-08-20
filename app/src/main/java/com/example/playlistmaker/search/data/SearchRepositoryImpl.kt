package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.SearchHistory
import com.example.playlistmaker.search.data.api.SearchRepository
import com.example.playlistmaker.search.data.network.TrackResponse
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
    private val searchHistory: SearchHistory
) : SearchRepository {
    override fun findTrack(request: String): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(request)
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Нет доступа к интернету. Проверьте подключение"))
            }

            200 -> {
                with(response as TrackResponse) {
                    val data = results.map {
                        Track(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl,
                            isFavorite = isFavoriteTrack(it.trackId)
                        )
                    } as ArrayList<Track>
                    emit(Resource.Success(data))
                }

            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }

    }

    override fun saveTrack(track: Track) {
        searchHistory.write(track)
    }


    override suspend fun showHistory(): List<Track> {
        var trackList = searchHistory.read()
        trackList.forEach {
            var id = appDatabase.trackDao().getTracksId(it.trackId)
            Log.i("FINDTHEWAY", "Работает функция showHistory в репозитории. id в базе = $id, trackId = ${it.trackId}")
            it.isFavorite = id == it.trackId
        }
        return trackList
    }

    override fun clearHistory() {
        searchHistory.clear()
    }

    private suspend fun isFavoriteTrack(trackId: Long): Boolean {
        val track: Long? = appDatabase.trackDao().getTracksId(trackId)
        Log.i("FINDTHEWAY", "Работает функция isFavoriteTrack. trackId = $trackId, track = $track")
        return trackId == track
    }
}