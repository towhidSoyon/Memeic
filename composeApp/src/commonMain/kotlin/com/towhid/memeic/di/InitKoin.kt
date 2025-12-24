package com.towhid.memeic.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? =null) {
    Napier.base(DebugAntilog())
    startKoin {
       config?.invoke(this)
       modules(appModule + dataModule)
    }
}