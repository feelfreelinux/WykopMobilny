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
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.File
import java.util.concurrent.TimeUnit

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

    @Provides
    @PathFixingInterceptor
    fun AppKeyReplacingInterceptor.pathFixingInterceptor(): Interceptor = this

    @Reusable
    @Provides
    fun retrofit(
        okHttpClient: OkHttpClient,
        @PathFixingInterceptor pathFixing: Interceptor,
        @SigningInterceptor signing: Interceptor,
        @BaseUrl apiUrl: String,
        cacheDir: File,
    ) =
        Retrofit.Builder()
            .client(
                okHttpClient.newBuilder()
                    .cache(Cache(cacheDir, maxSize = CACHE_SIZE))
                    .addInterceptor(pathFixing)
                    .addInterceptor(signing)
                    .protocols(listOf(Protocol.HTTP_1_1))
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .build(),
            )
            .baseUrl(apiUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    companion object {

        private const val CACHE_SIZE = 10 * 1024 * 1024L
        private const val DEFAULT_TIMEOUT = 30L
    }
}
