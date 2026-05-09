package com.example.pgonzalezmusicapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pgonzalezmusicapp.components.MiniPlayer
import com.example.pgonzalezmusicapp.models.Album
import com.example.pgonzalezmusicapp.services.AlbumsService
import com.example.pgonzalezmusicapp.ui.theme.PurpleDark
import com.example.pgonzalezmusicapp.ui.theme.PurpleMid
import androidx.compose.material.icons.filled.MoreVert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun DetailScreen(id: Int, navController: NavController) {
    val BASE_URL = "https://musicapi.pjasoft.com/"
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = async<Album>(Dispatchers.IO) {
                val service = retrofit.create(AlbumsService::class.java)
                service.getAlbumById(id)
            }

            Log.i("DetailScreen", result.await().toString())
            album = result.await()
            isLoading = false
        } catch (e: Exception) {
            Log.e("DetailScreen", e.message.toString())
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PurpleMid)
        }
    } else if (album == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Album no encontrado", color = Color.Gray)
        }
    } else {
        val tracks = (1..10).map { i -> "${album!!.title} • Track $i" }

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {

                item {
                    // Header con imagen grande y scrim morado
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        AsyncImage(
                            model = album!!.image,
                            contentDescription = album!!.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Scrim morado
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            PurpleDark.copy(alpha = 0.4f),
                                            PurpleDark.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )
                        // Botón back y favorito
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Favorite",
                                    tint = Color.White
                                )
                            }
                        }
                        // Título y artista
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = album!!.title,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = album!!.artist,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            // Botones play
                            Row {
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(PurpleMid, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = "Play2",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Card About this album
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "About this album",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = PurpleMid
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = album!!.description,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Chip del artista
                        SuggestionChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = "Artist: ${album!!.artist}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color(0xFFF3E5F5)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // 10 canciones ficticias
                items(tracks) { track ->
                    TrackItem(
                        trackTitle = track,
                        artist = album!!.artist,
                        image = album!!.image
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            MiniPlayer(album = album)
        }
    }
}

@Composable
fun TrackItem(trackTitle: String, artist: String, image: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = image,
            contentDescription = trackTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = trackTitle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = artist, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "More",
            tint = Color.Gray
        )
    }
}