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
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create

@Module(includes = [RetrofitModule::class])
internal class WykopModule {

    @Reusable
    @Provides
    fun addLinkRetrofitApi(retrofit: Retrofit) =
        retrofit.create<AddLinkRetrofitApi>()

    @Reusable
    @Provides
    fun entriesRetrofitApi(retrofit: Retrofit) =
        retrofit.create<EntriesRetrofitApi>()

    @Reusable
    @Provides
    fun externalRetrofitApi(retrofit: Retrofit) =
        retrofit.create<ExternalRetrofitApi>()

    @Reusable
    @Provides
    fun hitsRetrofitApi(retrofit: Retrofit) =
        retrofit.create<HitsRetrofitApi>()

    @Reusable
    @Provides
    fun linksRetrofitApi(retrofit: Retrofit) =
        retrofit.create<LinksRetrofitApi>()

    @Reusable
    @Provides
    fun loginRetrofitApi(retrofit: Retrofit) =
        retrofit.create<LoginRetrofitApi>()

    @Reusable
    @Provides
    fun myWykopRetrofitApi(retrofit: Retrofit) =
        retrofit.create<MyWykopRetrofitApi>()

    @Reusable
    @Provides
    fun notificationsRetrofitApi(retrofit: Retrofit) =
        retrofit.create<NotificationsRetrofitApi>()

    @Reusable
    @Provides
    fun pMRetrofitApi(retrofit: Retrofit) =
        retrofit.create<PMRetrofitApi>()

    @Reusable
    @Provides
    fun profileRetrofitApi(retrofit: Retrofit) =
        retrofit.create<ProfileRetrofitApi>()

    @Reusable
    @Provides
    fun searchRetrofitApi(retrofit: Retrofit) =
        retrofit.create<SearchRetrofitApi>()

    @Reusable
    @Provides
    fun suggestRetrofitApi(retrofit: Retrofit) =
        retrofit.create<SuggestRetrofitApi>()

    @Reusable
    @Provides
    fun tagRetrofitApi(retrofit: Retrofit) =
        retrofit.create<TagRetrofitApi>()
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
            .build()
}
