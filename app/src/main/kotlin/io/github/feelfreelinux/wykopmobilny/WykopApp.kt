package io.github.feelfreelinux.wykopmobilny

import android.content.Context
import android.support.multidex.MultiDex
import com.bugsnag.android.Bugsnag
import com.devbrackets.android.exomedia.ExoMedia
import com.evernote.android.job.JobManager
import com.github.piasy.biv.BigImageViewer
//import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.feelfreelinux.wykopmobilny.di.DaggerAppComponent
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationJobCreator
import okhttp3.OkHttpClient
import javax.inject.Inject


class WykopApp : DaggerApplication() {
    @Inject lateinit var jobCreator : WykopNotificationJobCreator
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    companion object {
        val WYKOP_API_URL = "https://a2.wykop.pl"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        ExoMedia.setDataSourceFactoryProvider({
            userAgent, listener ->
            OkHttpDataSourceFactory(OkHttpClient(), userAgent, listener)
        })
        AndroidThreeTen.init(this)
        JobManager.create(this).addJobCreator(jobCreator)
        //BigImageViewer.initialize(GlideImageLoader.with(applicationContext))
        if (!BuildConfig.DEBUG) {
            Bugsnag.init(this, "3b54bcbf963f13ae9aa8f8376ea7c1cc")
            Bugsnag.setReleaseStage(BuildConfig.BUILD_TYPE)
            Bugsnag.setProjectPackages("io.github.feelfreelinux.wykopmobilny")
        }
    }
}
