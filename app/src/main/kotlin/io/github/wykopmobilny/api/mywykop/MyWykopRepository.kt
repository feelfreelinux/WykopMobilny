package io.github.wykopmobilny.api.mywykop

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.MyWykopRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.models.dataclass.EntryLink
import io.github.wykopmobilny.models.mapper.apiv2.EntryLinkMapper
import io.reactivex.Single
import javax.inject.Inject

class MyWykopRepository @Inject constructor(
    private val myWykopApi: MyWykopRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter,
    private val patronsApi: PatronsApi
) : MyWykopApi {

    override fun getIndex(page: Int): Single<List<EntryLink>> =
        myWykopApi.getIndex(page)
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> EntryLinkMapper.map(response, owmContentFilter) } }

    override fun byUsers(page: Int): Single<List<EntryLink>> =
        myWykopApi.byUsers(page)
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> EntryLinkMapper.map(response, owmContentFilter) } }

    override fun byTags(page: Int): Single<List<EntryLink>> =
        myWykopApi.byTags(page)
            .retryWhen(userTokenRefresher)
            .flatMap { patronsApi.ensurePatrons(it) }
            .compose(ErrorHandlerTransformer())
            .map { it.map { response -> EntryLinkMapper.map(response, owmContentFilter) } }
}
