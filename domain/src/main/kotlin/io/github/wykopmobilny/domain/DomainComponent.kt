package io.github.wykopmobilny.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.domain.api.PagingSource
import io.github.wykopmobilny.domain.api.StoreMediator
import io.github.wykopmobilny.domain.promoted.GetPromotedQuery
import io.github.wykopmobilny.promoted.api.GetPromoted
import io.github.wykopmobilny.promoted.api.Link
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@Component(modules = [Queries::class, PromotedModule::class])
interface DomainComponent {

    fun promoted(): GetPromoted
}

@Module
internal class PromotedModule {

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

@Module
internal abstract class Queries {

    @Binds
    abstract fun GetPromotedQuery.bind(): GetPromoted
}
