package com.towhid.memeic.meme_editor.domain

interface SaveToStorageStrategy {
    fun getFilePath(fileName : String): String
}