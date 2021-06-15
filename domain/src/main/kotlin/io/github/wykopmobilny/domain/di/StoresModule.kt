package io.github.wykopmobilny.domain.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.blacklist.api.ScraperRetrofitApi
import io.github.wykopmobilny.storage.api.Blacklist
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import javax.inject.Singleton

@Module
internal class StoresModule {

    @Singleton
    @Provides
    fun blacklistStore(
        retrofitApi: ScraperRetrofitApi,
        storage: BlacklistPreferencesApi,
    ) = StoreBuilder.from<Unit, Blacklist, Blacklist>(
        fetcher = Fetcher.of {
            val api = retrofitApi.getBlacklist()
            Blacklist(
                tags = api.tags?.tags.orEmpty().map { it.tag.removePrefix("#") }.toSet(),
                users = api.users?.users.orEmpty().map { it.nick.removePrefix("@") }.toSet(),
            )
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { storage.blacklist },
            writer = { _, newValue -> storage.updateBlacklist(newValue) },
            delete = { storage.updateBlacklist(null) },
            deleteAll = { storage.updateBlacklist(null) },
        ),
    )
        .build()
}
