package com.example.pgonzalezmusicapp

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import okhttp3.OkHttpClient

class PGonzalezMusicApp : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory(
                    callFactory = {
                        OkHttpClient.Builder()
                            .addInterceptor { chain ->
                                val request = chain.request().newBuilder()
                                    .header(
                                        "User-Agent",
                                        "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                                    )
                                    .header("Referer", "https://en.wikipedia.org/")
                                    .header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                                    .build()
                                chain.proceed(request)
                            }
                            .build()
                    }
                ))
            }
            .crossfade(true)
            .build()
    }
}