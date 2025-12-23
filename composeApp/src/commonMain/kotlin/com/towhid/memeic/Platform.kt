package com.towhid.memeic

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform