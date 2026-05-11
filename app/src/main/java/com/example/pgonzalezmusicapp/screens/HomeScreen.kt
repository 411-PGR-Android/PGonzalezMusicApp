package com.example.pgonzalezmusicapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(navController: NavController) {
    val BASE_URL = "https://musicapi.pjasoft.com/"
    var albums by remember { mutableStateOf(listOf<Album>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = async<List<Album>>(Dispatchers.IO) {
                val service = retrofit.create(AlbumsService::class.java)
                service.getAllAlbums()
            }

            Log.i("HomeScreen", result.await().toString())
            albums = result.await()
            isLoading = false
        } catch (e: Exception) {
            Log.e("HomeScreen", e.message.toString())
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
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E5F5))
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF3E5F5))
            ) {

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(PurpleMid, PurpleDark)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                        ) {
                            // Iconos arriba
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = Color.White
                                )
                            }
                            // Texto abajo
                            Column(modifier = Modifier.padding(top = 40.dp)) {
                                Text(
                                    text = "Good Morning!",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Paulina González",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Albums", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "See more", fontSize = 13.sp, color = PurpleMid)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(albums) { album ->
                            AlbumCard(album = album, onClick = {
                                navController.navigate("detail/${album.id}")
                            })
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Recently Played", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "See more", fontSize = 13.sp, color = PurpleMid)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(albums) { album ->
                    RecentlyPlayedItem(album = album, onClick = {
                        navController.navigate("detail/${album.id}")
                    })
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3E5F5))
                    .padding(top = 4.dp, bottom = 16.dp)
            ) {
                MiniPlayer(album = albums.firstOrNull())
            }
        }
    }
}

@Composable
fun RecentlyPlayedItem(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = album.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(
                    text = "${album.artist} • Popular Song",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun AlbumCard(album: Album, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Cuadrito morado abajo
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 8.dp, vertical = 0.dp)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PurpleDark.copy(alpha = 0.55f))
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = album.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                    Text(
                        text = album.artist,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        tint = PurpleDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}