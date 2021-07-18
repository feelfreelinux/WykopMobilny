package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.Flow

interface UserPreferenceApi {

    fun get(key: String): Flow<String?>

    suspend fun update(key: String, newValue: String)

    suspend fun clear(key: String)
}
