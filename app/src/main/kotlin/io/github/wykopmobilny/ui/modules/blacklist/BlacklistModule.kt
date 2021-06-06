package io.github.wykopmobilny.ui.modules.blacklist

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.api.scraper.ScraperApi
import io.github.wykopmobilny.api.tag.TagApi
import io.github.wykopmobilny.base.Schedulers

@Module
class BlacklistModule {
    @Provides
    fun provideBlacklistPresenter(
        schedulers: Schedulers,
        scraperApi: ScraperApi,
        tagApi: TagApi,
        profileApi: ProfileApi
    ) = BlacklistPresenter(schedulers, scraperApi, tagApi, profileApi)
}
