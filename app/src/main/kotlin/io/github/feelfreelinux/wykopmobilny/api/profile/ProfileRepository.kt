package io.github.feelfreelinux.wykopmobilny.api.profile

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryCommentMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryLinkMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.EntryMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkCommentMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.RelatedMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObserveStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.RelatedResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.reactivex.Single
import retrofit2.Retrofit

class ProfileRepository(
    val retrofit: Retrofit,
    val userTokenRefresher: UserTokenRefresher,
    val owmContentFilter: OWMContentFilter,
    val blacklistPreferencesApi: BlacklistPreferencesApi
) : ProfileApi {
    private val profileApi by lazy { retrofit.create(ProfileRetrofitApi::class.java) }

    override fun getIndex(username: String): Single<ProfileResponse> =
        profileApi.getIndex(username)
            .retryWhen(userTokenRefresher)
            .compose<ProfileResponse>(ErrorHandlerTransformer())

    override fun getAdded(username: String, page: Int): Single<List<Link>> =
        profileApi.getAdded(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getActions(username: String): Single<List<EntryLink>> =
        profileApi.getActions(username)
            .retryWhen(userTokenRefresher)
            .compose<List<EntryLinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryLinkMapper.map(it, owmContentFilter) } }

    override fun getPublished(username: String, page: Int): Single<List<Link>> =
        profileApi.getPublished(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getEntries(username: String, page: Int): Single<List<Entry>> =
        profileApi.getEntries(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<EntryResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryMapper.map(it, owmContentFilter) } }

    override fun getEntriesComments(username: String, page: Int): Single<List<EntryComment>> =
        profileApi.getEntriesComments(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<EntryCommentResponse>>(ErrorHandlerTransformer())
            .map { it.map { EntryCommentMapper.map(it, owmContentFilter) } }

    override fun getLinkComments(username: String, page: Int): Single<List<LinkComment>> =
        profileApi.getLinkComments(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<LinkCommentResponse>>(ErrorHandlerTransformer())
            .map { it.map { LinkCommentMapper.map(it, owmContentFilter) } }

    override fun getBuried(username: String, page: Int): Single<List<Link>> =
        profileApi.getBuried(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getDigged(username: String, page: Int): Single<List<Link>> =
        profileApi.getDigged(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<LinkResponse>>(ErrorHandlerTransformer())
            .map { it.map { LinkMapper.map(it, owmContentFilter) } }

    override fun getBadges(username: String, page: Int): Single<List<BadgeResponse>> =
        profileApi.getBadges(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<BadgeResponse>>(ErrorHandlerTransformer())

    override fun getRelated(username: String, page: Int): Single<List<Related>> =
        profileApi.getRelated(username, page)
            .retryWhen(userTokenRefresher)
            .compose<List<RelatedResponse>>(ErrorHandlerTransformer())
            .map { it.map { RelatedMapper.map(it) } }

    override fun observe(tag: String) = profileApi.observe(tag)
        .retryWhen(userTokenRefresher)
        .compose<ObserveStateResponse>(ErrorHandlerTransformer())

    override fun unobserve(tag: String) = profileApi.unobserve(tag)
        .retryWhen(userTokenRefresher)
        .compose<ObserveStateResponse>(ErrorHandlerTransformer())

    override fun block(tag: String) = profileApi.block(tag)
        .retryWhen(userTokenRefresher)
        .compose<ObserveStateResponse>(ErrorHandlerTransformer())
        .doOnSuccess {
            if (!blacklistPreferencesApi.blockedUsers.contains(tag.removePrefix("@"))) {
                blacklistPreferencesApi.blockedUsers = blacklistPreferencesApi.blockedUsers.plus(tag.removePrefix("@"))
            }
        }

    override fun unblock(tag: String) = profileApi.unblock(tag)
        .retryWhen(userTokenRefresher)
        .compose<ObserveStateResponse>(ErrorHandlerTransformer())
        .doOnSuccess {
            if (blacklistPreferencesApi.blockedUsers.contains(tag.removePrefix("@"))) {
                blacklistPreferencesApi.blockedUsers = blacklistPreferencesApi.blockedUsers.minus(tag.removePrefix("@"))
            }
        }
}
