package com.towhid.memeic.meme_editor.data

import com.towhid.memeic.meme_editor.domain.SaveToStorageStrategy

expect class CacheStorageStrategy : SaveToStorageStrategy {
    override fun getFilePath(fileName: String): String
}