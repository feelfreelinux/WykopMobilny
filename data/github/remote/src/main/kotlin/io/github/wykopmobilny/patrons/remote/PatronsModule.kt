package io.github.wykopmobilny.patrons.remote

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.wykopmobilny.patrons.api.PatronsRetrofitApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create

@Module
internal class PatronsModule {

    @Reusable
    @Provides
    fun retrofit(okHttpClient: OkHttpClient, baseUrl: String) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Reusable
    @Provides
    fun patronsApi(retrofit: Retrofit) = retrofit.create<PatronsRetrofitApi>()
}
