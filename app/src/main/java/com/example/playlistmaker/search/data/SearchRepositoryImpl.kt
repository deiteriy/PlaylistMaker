package com.example.playlistmaker.search.data

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


    override fun showHistory(): List<Track> {
        return searchHistory.read()
    }

    override fun clearHistory() {
        searchHistory.clear()
    }

}