package io.github.wykopmobilny.utils

import androidx.annotation.MainThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

data class Event<T : Any>(
    private val value: T,
) {

    private var hasBeenConsumed = false

    @MainThread
    fun consume(consumer: (T) -> Unit) {
        if (!hasBeenConsumed) {
            hasBeenConsumed = true
            consumer(value)
        }
    }
}

fun <T : Any> T.toEvent() = Event(this)

suspend fun <T : Any> Flow<Event<T>>.collectEvent(block: (T) -> Unit) =
    collect { it.consume(block) }
