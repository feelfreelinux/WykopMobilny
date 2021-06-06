package io.github.wykopmobilny.blacklist.remote

import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.wykopmobilny.blacklist.api.ScraperRetrofitApi
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.create

@Module
internal class ScraperModule {

    @Provides
    @Reusable
    fun asd(retrofit: Retrofit) = retrofit.create<ScraperRetrofitApi>()

    @Provides
    @Reusable
    fun createScraperRetrofit(
        okHttpClient: OkHttpClient,
        url: String,
        interceptor: ScraperInterceptor,
    ): Retrofit {
        val client = okHttpClient.newBuilder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(JspoonConverterFactory.create())
            .build()
    }
}
