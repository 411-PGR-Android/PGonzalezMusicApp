package com.example.pgonzalezmusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.pgonzalezmusicapp.screens.DetailScreen
import com.example.pgonzalezmusicapp.screens.HomeScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pgonzalezmusicapp.ui.theme.PGonzalezMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PGonzalezMusicAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = "home") {
                            HomeScreen(navController = navController)
                        }
                        composable(
                            route = "detail/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) { backStack ->
                            val id = backStack.arguments?.getString("id") ?: ""
                            DetailScreen(id = id, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
