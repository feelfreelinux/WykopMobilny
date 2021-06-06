package io.github.wykopmobilny.patrons.remote

import dagger.BindsInstance
import dagger.Component
import io.github.wykopmobilny.patrons.api.PatronsRetrofitApi
import okhttp3.OkHttpClient

@Component(modules = [PatronsModule::class])
interface PatronsComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance okHttpClient: OkHttpClient,
            @BindsInstance baseUrl: String,
        ): PatronsComponent
    }

    fun patronsApi(): PatronsRetrofitApi
}
