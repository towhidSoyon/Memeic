package com.towhid.memeic.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.towhid.memeic.meme_editor.presentation.MemeEditorViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.towhid.memeic.api.apis.PostApi
import com.towhid.memeic.meme_gallery.presentation.MemeGalleryViewModel
import io.ktor.serialization.kotlinx.json.json


expect val platformAppModule: Module
val appModule = module {
    viewModelOf(::MemeEditorViewModel)
    viewModelOf(::MemeGalleryViewModel)
    includes(platformAppModule)
}

val dataModule = module {

    single<Json> {
        Json {
            ignoreUnknownKeys = true // Ignores unknown fields in JSON
            isLenient = true // Allows flexibility in JSON parsing
            prettyPrint = true
            encodeDefaults = true
        }
    }


    single {
        val json: Json = get()

        HttpClient{ // ✅ engine REQUIRED
            install(ContentNegotiation) {
                json(get<Json>()) // Use the Json instance from Koin
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {

                    }
                }
            }

            expectSuccess = true
        }
    }

    single<PostApi> {
        PostApi(
            baseUrl = "https://jsonplaceholder.typicode.com/",
            httpClient = get()
        )
    }

}