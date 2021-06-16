package io.github.wykopmobilny.domain.navigation.android

import android.app.Application
import android.content.Intent
import io.github.wykopmobilny.domain.navigation.AppRestarter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AndroidAppRestarter @Inject constructor(
    private val application: Application,
) : AppRestarter {

    override suspend fun restart(): Unit = withContext(Dispatchers.Main) {
        application.startActivity(
            Intent(
                application,
                Class.forName("io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity"),
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            },
        )
    }
}
