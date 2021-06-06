package io.github.wykopmobilny.api.tag

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.TagRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.ObservedTagResponse
import io.github.wykopmobilny.api.responses.TagEntriesResponse
import io.github.wykopmobilny.api.responses.TagLinksResponse
import io.github.wykopmobilny.models.mapper.apiv2.TagEntriesMapper
import io.github.wykopmobilny.models.mapper.apiv2.TagLinksMapper
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class TagRepository @Inject constructor(
    private val tagApi: TagRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter,
    private val blacklistPreferencesApi: BlacklistPreferencesApi,
) : TagApi {

    override fun getTagEntries(tag: String, page: Int) = rxSingle { tagApi.getTagEntries(tag, page) }
        .retryWhen(userTokenRefresher)
        .flatMap(ErrorHandler<TagEntriesResponse>())
        .map { TagEntriesMapper.map(it, owmContentFilter) }

    override fun getTagLinks(tag: String, page: Int) = rxSingle { tagApi.getTagLinks(tag, page) }
        .retryWhen(userTokenRefresher)
        .flatMap(ErrorHandler<TagLinksResponse>())
        .map { TagLinksMapper.map(it, owmContentFilter) }

    override fun getObservedTags(): Single<List<ObservedTagResponse>> =
        rxSingle { tagApi.getObservedTags() }
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun observe(tag: String) = rxSingle { tagApi.observe(tag) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun unobserve(tag: String) = rxSingle { tagApi.unobserve(tag) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun block(tag: String) = rxSingle { tagApi.block(tag) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .doOnSuccess {
            if (!blacklistPreferencesApi.blockedTags.contains(tag.removePrefix("#"))) {
                blacklistPreferencesApi.blockedTags = blacklistPreferencesApi.blockedTags.plus(tag.removePrefix("#"))
            }
        }

    override fun unblock(tag: String) = rxSingle { tagApi.unblock(tag) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .doOnSuccess {
            if (blacklistPreferencesApi.blockedTags.contains(tag.removePrefix("#"))) {
                blacklistPreferencesApi.blockedTags = blacklistPreferencesApi.blockedTags.minus(tag.removePrefix("#"))
            }
        }
}
