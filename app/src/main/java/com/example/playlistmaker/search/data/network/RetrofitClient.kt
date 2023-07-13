package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.domain.NetworkError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitClient(private val api: RetrofitApi) : NetworkClient {
    override fun doRequest(
        request: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        api.search(request)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                onSuccess.invoke(response.body()?.results!!)

                            } else {
                                onError.invoke(NetworkError.NOTHING_FOUND)
                            }

                        }

                        else -> onError.invoke(NetworkError.NO_INTERNET)
                    }

                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    onError.invoke(NetworkError.NO_INTERNET)
                }

            })
    }
}
