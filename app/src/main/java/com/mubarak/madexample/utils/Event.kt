package com.mubarak.madexample.utils


/**
 * A wrapper class livedata events*/
 class Event<T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
           null
        } else {
            hasBeenHandled = true
            content
        }
    }

}

