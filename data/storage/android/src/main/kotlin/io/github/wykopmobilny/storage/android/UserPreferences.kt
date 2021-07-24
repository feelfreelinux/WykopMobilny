package io.github.wykopmobilny.storage.android

import android.content.Context
import androidx.core.content.edit
import dagger.Reusable
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import javax.inject.Inject

@Reusable
internal class UserPreferences @Inject constructor(
    context: Context,
    executor: Executor,
) : BasePreferences(context, executor, true), UserPreferenceApi {

    override fun get(key: String): Flow<String?> =
        preferences
            .filter { it == key }
            .map {
                prefs.getString(key, null)
            }
            .onStart { emit(prefs.getString(key, null)) }

    override suspend fun update(key: String, newValue: String) = withContext(Dispatchers.IO) {
        prefs.edit { putString(key, newValue) }
    }

    override suspend fun clear(key: String) = withContext(Dispatchers.IO) {
        prefs.edit { remove(key) }
    }
}
