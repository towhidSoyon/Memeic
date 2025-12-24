package com.towhid.memeic.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.towhid.memeic.meme_editor.presentation.MemeEditorRoot
import com.towhid.memeic.meme_gallery.presentation.MemeGallery
import com.towhid.memeic.meme_gallery.presentation.MemeGalleryScreen
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.MemeGallery
    ) {
        composable<Route.MemeGallery> {
            MemeGallery(
                onMemeTemplateSelected = { memeTemplate ->
                    navController.navigate(Route.MemeEditor(memeTemplate.id))
                }
            )
        }
        composable<Route.MemeEditor> {
            val templateId = it.toRoute<Route.MemeEditor>().templateId
            val template = remember(templateId) {
                memeTemplates.first { it.id == templateId }
            }
            MemeEditorRoot(
                template = template,
                onGoBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

@Serializable
sealed interface Route {

    @Serializable
    data object MemeGallery: Route

    @Serializable
    data class MemeEditor(
        val templateId: String
    ): Route
}