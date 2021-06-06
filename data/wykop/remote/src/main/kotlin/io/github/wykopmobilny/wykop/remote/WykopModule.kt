package io.github.wykopmobilny.wykop.remote

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.wykopmobilny.api.endpoints.AddLinkRetrofitApi
import io.github.wykopmobilny.api.endpoints.EntriesRetrofitApi
import io.github.wykopmobilny.api.endpoints.ExternalRetrofitApi
import io.github.wykopmobilny.api.endpoints.HitsRetrofitApi
import io.github.wykopmobilny.api.endpoints.LinksRetrofitApi
import io.github.wykopmobilny.api.endpoints.LoginRetrofitApi
import io.github.wykopmobilny.api.endpoints.MyWykopRetrofitApi
import io.github.wykopmobilny.api.endpoints.NotificationsRetrofitApi
import io.github.wykopmobilny.api.endpoints.PMRetrofitApi
import io.github.wykopmobilny.api.endpoints.ProfileRetrofitApi
import io.github.wykopmobilny.api.endpoints.SearchRetrofitApi
import io.github.wykopmobilny.api.endpoints.SuggestRetrofitApi
import io.github.wykopmobilny.api.endpoints.TagRetrofitApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

@Module(includes = [RetrofitModule::class])
internal class WykopModule {

    @Reusable
    @Provides
    fun addLinkRetrofitApi(retrofit: Retrofit) =
        retrofit.create(AddLinkRetrofitApi::class.java)

    @Reusable
    @Provides
    fun entriesRetrofitApi(retrofit: Retrofit) =
        retrofit.create(EntriesRetrofitApi::class.java)

    @Reusable
    @Provides
    fun externalRetrofitApi(retrofit: Retrofit) =
        retrofit.create(ExternalRetrofitApi::class.java)

    @Reusable
    @Provides
    fun hitsRetrofitApi(retrofit: Retrofit) =
        retrofit.create(HitsRetrofitApi::class.java)

    @Reusable
    @Provides
    fun linksRetrofitApi(retrofit: Retrofit) =
        retrofit.create(LinksRetrofitApi::class.java)

    @Reusable
    @Provides
    fun loginRetrofitApi(retrofit: Retrofit) =
        retrofit.create(LoginRetrofitApi::class.java)

    @Reusable
    @Provides
    fun myWykopRetrofitApi(retrofit: Retrofit) =
        retrofit.create(MyWykopRetrofitApi::class.java)

    @Reusable
    @Provides
    fun notificationsRetrofitApi(retrofit: Retrofit) =
        retrofit.create(NotificationsRetrofitApi::class.java)

    @Reusable
    @Provides
    fun pMRetrofitApi(retrofit: Retrofit) =
        retrofit.create(PMRetrofitApi::class.java)

    @Reusable
    @Provides
    fun profileRetrofitApi(retrofit: Retrofit) =
        retrofit.create(ProfileRetrofitApi::class.java)

    @Reusable
    @Provides
    fun searchRetrofitApi(retrofit: Retrofit) =
        retrofit.create(SearchRetrofitApi::class.java)

    @Reusable
    @Provides
    fun suggestRetrofitApi(retrofit: Retrofit) =
        retrofit.create(SuggestRetrofitApi::class.java)

    @Reusable
    @Provides
    fun tagRetrofitApi(retrofit: Retrofit) =
        retrofit.create(TagRetrofitApi::class.java)
}

@Module
internal class RetrofitModule {

    @Reusable
    @Provides
    fun retrofit(okHttpClient: OkHttpClient, apiUrl: String) =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(apiUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}
