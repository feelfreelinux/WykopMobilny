package io.github.wykopmobilny.domain.blacklist.di

import dagger.Binds
import dagger.Module
import io.github.wykopmobilny.domain.blacklist.GetBlacklistDetailsQuery
import io.github.wykopmobilny.ui.blacklist.GetBlacklistDetails

@Module
internal abstract class BlacklistModule {

    @Binds
    abstract fun GetBlacklistDetailsQuery.getBlacklistDetails(): GetBlacklistDetails
}
