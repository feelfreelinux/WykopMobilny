package io.github.wykopmobilny.domain.promoted

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.domain.api.PagingSource
import io.github.wykopmobilny.domain.api.StoreMediator
import io.github.wykopmobilny.promoted.api.GetPromoted
import io.github.wykopmobilny.promoted.api.Link
import javax.inject.Provider

@Module
internal abstract class PromotedModule {

    @Binds
    abstract fun GetPromotedQuery.bind(): GetPromoted

    companion object {

        @OptIn(ExperimentalPagingApi::class)
        @Provides
        fun promotedPager(
            mediator: StoreMediator<Link>,
            pagingSource: Provider<PagingSource<Link>>,
        ) = Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = mediator,
            pagingSourceFactory = pagingSource::get,
        )
    }
}
