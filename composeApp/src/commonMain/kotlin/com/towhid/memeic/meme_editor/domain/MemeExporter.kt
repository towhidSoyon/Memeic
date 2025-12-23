package com.towhid.memeic.meme_editor.domain

import androidx.compose.ui.unit.IntSize
import com.towhid.memeic.meme_editor.presentation.MemeText
import kotlin.js.JsFileName
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface MemeExporter {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun exportMeme(
        backgroundImageBytes : ByteArray,
        memeTexts: List<MemeText>,
        templateSize: IntSize,
        saveToStorageStrategy: SaveToStorageStrategy,
        fileName: String = "meme_${Uuid.random()}.jpg"
    ) : Result<String>
}