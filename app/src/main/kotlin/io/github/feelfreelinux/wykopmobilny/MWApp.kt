package io.github.feelfreelinux.wykopmobilny

import android.app.Application
import io.github.feelfreelinux.wykopmobilny.utils.Preferences

class MWApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)
    }
}
