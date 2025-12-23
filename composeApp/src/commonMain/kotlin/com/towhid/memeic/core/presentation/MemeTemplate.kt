package com.towhid.memeic.core.presentation

import memeic.composeapp.generated.resources.Res
import memeic.composeapp.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.DrawableResource

data class MemeTemplate(
    val id: String,
    val drawable: DrawableResource
)

val memeTemplates = Res
    .allDrawableResources
    .filterKeys { it.startsWith("meme_template") }
    .map { (key, value) ->
        MemeTemplate(
            id = key,
            drawable = value
        )
    }