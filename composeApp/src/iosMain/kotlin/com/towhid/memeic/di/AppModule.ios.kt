package com.towhid.memeic.di

import com.towhid.memeic.meme_editor.data.CacheStorageStrategy
import com.towhid.memeic.meme_editor.data.PlatformMemeExporter
import com.towhid.memeic.meme_editor.domain.MemeExporter
import com.towhid.memeic.meme_editor.domain.SaveToStorageStrategy
import com.towhid.memeic.meme_editor.presentation.util.PlatformShareSheet
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformAppModule = module {
    factoryOf(:: PlatformMemeExporter) bind MemeExporter :: class
    factoryOf(:: CacheStorageStrategy) bind SaveToStorageStrategy :: class
    factoryOf(::PlatformShareSheet)
}