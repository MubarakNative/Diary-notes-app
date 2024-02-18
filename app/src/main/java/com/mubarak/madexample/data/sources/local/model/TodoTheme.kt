package com.mubarak.madexample.data.sources.local.model

enum class TodoTheme(override val value: String): ValueEnum<String> {
    LIGHT("light"),
    DARK("dark"),
    FOLLOW_SYSTEM("system");

    companion object {
        fun fromValue(value: String): TodoTheme = findValueEnum(value)
    }
}