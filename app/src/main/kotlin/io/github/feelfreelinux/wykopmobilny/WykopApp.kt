package io.github.feelfreelinux.wykopmobilny

import com.devbrackets.android.exomedia.ExoMedia
import com.evernote.android.job.JobManager
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.support.DaggerApplication
import io.github.feelfreelinux.wykopmobilny.di.DaggerAppComponent
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationJobCreator
import okhttp3.OkHttpClient
import javax.inject.Inject

class WykopApp : DaggerApplication() {

    companion object {

        const val WYKOP_API_URL = "https://a2.wykop.pl"
    }

    @Inject
    lateinit var jobCreator: WykopNotificationJobCreator

    override fun applicationInjector() = DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        ExoMedia.setDataSourceFactoryProvider { userAgent, listener ->
            OkHttpDataSourceFactory(OkHttpClient(), userAgent, listener)
        }
        AndroidThreeTen.init(this)
        JobManager.create(this).addJobCreator(jobCreator)
    }
}
