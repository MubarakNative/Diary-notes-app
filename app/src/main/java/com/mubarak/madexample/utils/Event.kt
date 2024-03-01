package com.mubarak.madexample.utils

/**
 * A wrapper class for livedata events
 * Taken from [https://github.com/android/architecture-samples/]
 * */
 class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? { // this function help to get the content
        return if (hasBeenHandled) {
           null
        } else {
            hasBeenHandled = true
            content
        }
    }

}

