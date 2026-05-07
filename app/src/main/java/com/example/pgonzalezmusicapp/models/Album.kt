package com.example.pgonzalezmusicapp.models

import java.io.Serializable

data class Album(
    val id: Int,
    val title: String,
    val artist: String,
    val description: String,
    val image: String
) : Serializable