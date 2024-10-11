package com.example.userrepo.base.data

class Event<out T : Any>(private val content: T) {
    private var isDone = false

    private fun getIfNotDone(): T? {
        return if (isDone) {
            null
        } else {
            isDone = true
            content
        }
    }

    fun handle(event: (T) -> Unit) {
        getIfNotDone()?.let(event)
    }
}