package io.github.wykopmobilny.domain.settings

import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.time.Duration

internal fun <T> UserPreferenceApi.get(key: UserSetting<T>): Flow<T?> =
    get(key.preferencesKey)
        .map { value ->
            value?.let(key.mapping)
        }
        .distinctUntilChanged()

internal suspend fun <T> UserPreferenceApi.update(key: UserSetting<T>, value: T?) {
    val mapped = value?.let(key.reverseMapping)
    if (mapped == null) {
        clear(key.preferencesKey)
    } else {
        update(key.preferencesKey, mapped)
    }
}

internal class UserSetting<T>(
    val preferencesKey: String,
    val mapping: (String) -> T?,
    val reverseMapping: (T) -> String?,
)

internal fun <T : Enum<T>> enumMapping(
    preferencesKey: String,
    enumMapping: Map<T, String>,
) = UserSetting(
    preferencesKey = preferencesKey,
    mapping = { enumMapping.entries.firstOrNull { (_, value) -> value == it }?.key },
    reverseMapping = { enumMapping[it] },
)

internal fun booleanMapping(
    preferencesKey: String,
) = UserSetting(
    preferencesKey = preferencesKey,
    mapping = { it.toBoolean() },
    reverseMapping = { it.toString() },
)

internal fun durationMapping(
    preferencesKey: String,
) = UserSetting(
    preferencesKey = preferencesKey,
    mapping = { it.toLongOrNull()?.let(Duration.Companion::milliseconds) },
    reverseMapping = { it.inWholeMilliseconds.toString() },
)

internal fun longMapping(
    preferencesKey: String,
) = UserSetting(
    preferencesKey = preferencesKey,
    mapping = { it.toLongOrNull() },
    reverseMapping = { it.toString() },
)
