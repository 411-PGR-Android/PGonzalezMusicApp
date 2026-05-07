package com.example.pgonzalezmusicapp.services

import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumsService {

    @GET("api/albums")
    suspend fun getAllAlbums() : List<`Album.kt`>

    @GET("api/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int) : `Album.kt`
}