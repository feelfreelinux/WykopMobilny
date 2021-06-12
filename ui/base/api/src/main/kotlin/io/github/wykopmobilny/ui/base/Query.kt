package io.github.wykopmobilny.ui.base

import kotlinx.coroutines.flow.Flow

interface Query<T> {

    operator fun invoke(): Flow<T>
}
