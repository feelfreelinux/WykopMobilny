package io.github.wykopmobilny.api.profile

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.ProfileRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.BadgeResponse
import io.github.wykopmobilny.api.responses.ProfileResponse
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.EntryLink
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.models.dataclass.Related
import io.github.wykopmobilny.models.mapper.apiv2.EntryCommentMapper
import io.github.wykopmobilny.models.mapper.apiv2.EntryLinkMapper
import io.github.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.wykopmobilny.models.mapper.apiv2.LinkCommentMapper
import io.github.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.wykopmobilny.models.mapper.apiv2.RelatedMapper
import io.github.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.reactivex.Single
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher,
    private val owmContentFilter: OWMContentFilter,
    private val blacklistPreferencesApi: BlacklistPreferencesApi
) : ProfileApi {

    override fun getIndex(username: String): Single<ProfileResponse> =
        profileApi.getIndex(username)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun getAdded(username: String, page: Int): Single<List<Link>> =
        profileApi.getAdded(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getActions(username: String): Single<List<EntryLink>> =
        profileApi.getActions(username)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { EntryLinkMapper.map(it, owmContentFilter) } }

    override fun getPublished(username: String, page: Int): Single<List<Link>> =
        profileApi.getPublished(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getEntries(username: String, page: Int): Single<List<Entry>> =
        profileApi.getEntries(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it, owmContentFilter) } }

    override fun getEntriesComments(username: String, page: Int): Single<List<EntryComment>> =
        profileApi.getEntriesComments(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { EntryCommentMapper.map(it, owmContentFilter) } }

    override fun getLinkComments(username: String, page: Int): Single<List<LinkComment>> =
        profileApi.getLinkComments(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { LinkCommentMapper.map(it, owmContentFilter) } }

    override fun getBuried(username: String, page: Int): Single<List<Link>> =
        profileApi.getBuried(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getDigged(username: String, page: Int): Single<List<Link>> =
        profileApi.getDigged(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getBadges(username: String, page: Int): Single<List<BadgeResponse>> =
        profileApi.getBadges(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())

    override fun getRelated(username: String, page: Int): Single<List<Related>> =
        profileApi.getRelated(username, page)
            .retryWhen(userTokenRefresher)
            .compose(ErrorHandlerTransformer())
            .map { it.map { RelatedMapper.map(it) } }

    override fun observe(tag: String) = profileApi.observe(tag)
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun unobserve(tag: String) = profileApi.unobserve(tag)
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun block(tag: String) = profileApi.block(tag)
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .doOnSuccess {
            if (!blacklistPreferencesApi.blockedUsers.contains(tag.removePrefix("@"))) {
                blacklistPreferencesApi.blockedUsers = blacklistPreferencesApi.blockedUsers.plus(tag.removePrefix("@"))
            }
        }

    override fun unblock(tag: String) = profileApi.unblock(tag)
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .doOnSuccess {
            if (blacklistPreferencesApi.blockedUsers.contains(tag.removePrefix("@"))) {
                blacklistPreferencesApi.blockedUsers = blacklistPreferencesApi.blockedUsers.minus(tag.removePrefix("@"))
            }
        }
}
