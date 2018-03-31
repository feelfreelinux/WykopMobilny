package io.github.feelfreelinux.wykopmobilny.api.profile

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.*
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferences
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.reactivex.Single
import retrofit2.Retrofit

class ProfileRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher, val linksPreferencesApi : LinksPreferencesApi, val blacklistPreferencesApi: BlacklistPreferencesApi, val settingsPreferencesApi: SettingsPreferencesApi) : ProfileApi {
    private val profileApi by lazy { retrofit.create(ProfileRetrofitApi::class.java) }

    override fun getIndex(username : String): Single<ProfileResponse> =
            profileApi.getIndex(username)
                    .retryWhen(userTokenRefresher)
                    .compose<ProfileResponse>(ErrorHandlerTransformer())

    override fun getAdded(username : String, page : Int): Single<List<Link>> =
            profileApi.getAdded(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getActions(username : String): Single<List<EntryLink>> =
            profileApi.getActions(username)
                    .retryWhen(userTokenRefresher)
                    .compose<List<EntryLinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { EntryLinkMapper.map(it, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getPublished(username : String, page : Int): Single<List<Link>> =
            profileApi.getPublished(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getEntries(username : String, page : Int): Single<List<Entry>> =
            profileApi.getEntries(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<EntryResponse>>(ErrorHandlerTransformer())
                    .map { it.map { EntryMapper.map(it, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getEntriesComments(username: String, page: Int): Single<List<EntryComment>> =
            profileApi.getEntriesComments(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<EntryCommentResponse>>(ErrorHandlerTransformer())
                    .map { it.map { EntryCommentMapper.map(it, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getLinkComments(username: String, page: Int): Single<List<LinkComment>> =
            profileApi.getLinkComments(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkCommentResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkCommentMapper.map(it, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getBuried(username : String, page : Int): Single<List<Link>> =
            profileApi.getBuried(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getDigged(username : String, page : Int): Single<List<Link>> =
            profileApi.getDigged(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<LinkResponse>>(ErrorHandlerTransformer())
                    .map { it.map { LinkMapper.map(it, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi) } }

    override fun getBadges(username: String, page: Int): Single<List<BadgeResponse>> =
            profileApi.getBadges(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<BadgeResponse>>(ErrorHandlerTransformer())


    override fun getRelated(username: String, page: Int): Single<List<Related>> =
            profileApi.getRelated(username, page)
                    .retryWhen(userTokenRefresher)
                    .compose<List<RelatedResponse>>(ErrorHandlerTransformer())
                    .map { it.map { RelatedMapper.map(it) } }

    override fun observe(tag : String) = profileApi.observe(tag)
            .retryWhen(userTokenRefresher)
            .compose<ObserveStateResponse>(ErrorHandlerTransformer())

    override fun unobserve(tag : String) = profileApi.unobserve(tag)
            .retryWhen(userTokenRefresher)
            .compose<ObserveStateResponse>(ErrorHandlerTransformer())

    override fun block(tag : String) = profileApi.block(tag)
            .retryWhen(userTokenRefresher)
            .compose<ObserveStateResponse>(ErrorHandlerTransformer())
            .doOnSuccess {
                if (!blacklistPreferencesApi.blockedUsers.contains(tag.removePrefix("@")))
                    blacklistPreferencesApi.blockedUsers = blacklistPreferencesApi.blockedUsers.plus(tag.removePrefix("@"))
            }

    override fun unblock(tag : String) = profileApi.unblock(tag)
            .retryWhen(userTokenRefresher)
            .compose<ObserveStateResponse>(ErrorHandlerTransformer())
            .doOnSuccess {
                if (blacklistPreferencesApi.blockedUsers.contains(tag.removePrefix("@")))
                    blacklistPreferencesApi.blockedUsers = blacklistPreferencesApi.blockedUsers.minus(tag.removePrefix("@"))
            }
}