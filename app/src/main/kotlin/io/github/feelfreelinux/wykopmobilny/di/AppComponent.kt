package io.github.feelfreelinux.wykopmobilny.di

import android.app.Application
import android.content.Context
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = arrayOf(AppModule::class) )
interface AppComponent {
    val application : Application
    val context : Context
}