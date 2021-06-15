package io.github.wykopmobilny.domain.navigation.android

import android.app.Application
import android.content.res.Configuration
import io.github.wykopmobilny.domain.navigation.NightModeDetector
import io.github.wykopmobilny.domain.navigation.NightModeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AndroidNightModeDetector @Inject constructor(
    private val application: Application,
) : NightModeDetector {

    override suspend fun getNightModeState() = withContext(Dispatchers.Default) {
        when (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> NightModeState.Enabled
            Configuration.UI_MODE_NIGHT_NO -> NightModeState.Disabled
            Configuration.UI_MODE_NIGHT_UNDEFINED -> NightModeState.Unknown
            else -> NightModeState.Unknown
        }
    }
}
