package com.mubarak.madexample.data.sources.local.model

import java.lang.IllegalStateException

interface ValueEnum<T> {
    val value: T
}

inline fun <reified V : ValueEnum<out T>, T> findValueEnum(value: T) =
    V::class.java.enumConstants?.find { it.value == value } ?: throw IllegalStateException("Can't find value's")
