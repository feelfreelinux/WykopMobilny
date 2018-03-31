package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class BlacklistModule {
    @Provides
    fun provideBlacklistPresenter(schedulers: Schedulers, scraperApi: ScraperApi, tagApi: TagApi, profileApi: ProfileApi)
            = BlacklistPresenter(schedulers, scraperApi, tagApi, profileApi)
}