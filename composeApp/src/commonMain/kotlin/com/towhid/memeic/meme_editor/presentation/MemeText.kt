package com.towhid.memeic.meme_editor.presentation

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class MemeText(
    val id: String,
    val text: String,
    val fontSize: TextUnit = 36.sp,
    val offsetRatioX: Float = 0f,
    val offsetRatioY: Float = 0f,
    val rotation: Float = 0f,
    val scale: Float = 1f
)

sealed interface TextBoxInteractionState {
    data object None: TextBoxInteractionState
    data class Selected(val textBoxId: String): TextBoxInteractionState
    data class Editing(val textBoxId: String): TextBoxInteractionState
}