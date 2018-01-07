package io.github.feelfreelinux.wykopmobilny.api.search

import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.AuthorMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import retrofit2.Retrofit

class SearchRepository(val retrofit: Retrofit) : SearchApi {
    private val searchApi by lazy { retrofit.create(SearchRetrofitApi::class.java) }

    override fun searchLinks(page: Int, query: String) = searchApi
            .searchLinks(page, query)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it) } }

    override fun searchEntries(page: Int, query: String) = searchApi
            .searchEntries(page, query)
            .compose<List<EntryResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it) } }

    override fun searchProfiles(query: String) = searchApi
            .searchProfiles(query)
            .compose<List<AuthorResponse>>(ErrorHandlerTransformer())
            .map { it.map { AuthorMapper.map(it) } }
}