package idt.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform