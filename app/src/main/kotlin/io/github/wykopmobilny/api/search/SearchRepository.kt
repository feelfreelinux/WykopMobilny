package io.github.wykopmobilny.api.search

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.SearchRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.models.mapper.apiv2.AuthorMapper
import io.github.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.wykopmobilny.models.mapper.apiv2.LinkMapper
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter,
    private val patronsApi: PatronsApi,
) : SearchApi {

    override fun searchLinks(page: Int, query: String) = rxSingle { searchApi.searchLinks(page, query) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> LinkMapper.map(response, owmContentFilter) } }

    override fun searchEntries(page: Int, query: String) = rxSingle { searchApi.searchEntries(page, query) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> EntryMapper.map(response, owmContentFilter) } }

    override fun searchProfiles(query: String) = rxSingle { searchApi.searchProfiles(query) }
        .retryWhen(userTokenRefresher)
        .flatMap { patronsApi.ensurePatrons(it) }
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> AuthorMapper.map(response) } }
}
