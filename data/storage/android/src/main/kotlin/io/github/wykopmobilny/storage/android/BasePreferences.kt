package io.github.wykopmobilny.storage.android

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import java.util.concurrent.Executor
import kotlin.reflect.KProperty

@Suppress("UnnecessaryAbstractClass")
internal abstract class BasePreferences(
    private val context: Context,
    executor: Executor,
    useDefaultFile: Boolean = false,
) {

    protected val coroutineScope = CoroutineScope(Job() + executor.asCoroutineDispatcher())

    protected val prefs: SharedPreferences by lazy {
        if (useDefaultFile) {
            PreferenceManager.getDefaultSharedPreferences(context)
        } else {
            context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        }
    }
    protected val preferences = callbackFlow<String?> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> trySend(key) }
        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
        .shareIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
            replay = 1,
        )

    abstract class PrefDelegate<T>(val key: String) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    fun stringPref(key: String) = StringPrefDelegate(key)

    inner class StringPrefDelegate(prefKey: String) : PrefDelegate<String?>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String? = prefs.getString(key, null)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            prefs.edit().putString(key, value).apply()
        }
    }

    fun intPref(key: String) = IntPrefDelegate(key)

    inner class IntPrefDelegate(prefKey: String) : PrefDelegate<Int?>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int? =
            if (prefs.contains(key)) {
                prefs.getInt(key, 0)
            } else {
                null
            }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int?) {
            prefs.edit {
                value?.let { putInt(key, it) } ?: remove(key)
            }
        }
    }

    fun booleanPref(key: String, defaultValue: Boolean = false) = object : PrefDelegate<Boolean>(key) {

        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            prefs.getBoolean(key, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            prefs.edit {
                putBoolean(key, value)
            }
        }
    }

    fun stringSetPref(key: String) = StringSetPrefDelegate(key)

    inner class StringSetPrefDelegate(prefKey: String) : PrefDelegate<Set<String>?>(prefKey) {

        override fun getValue(thisRef: Any?, property: KProperty<*>): Set<String>? =
            prefs.getStringSet(key, null)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>?) {
            prefs.edit {
                value?.let { putStringSet(key, it) } ?: remove(key)
            }
        }
    }
}
