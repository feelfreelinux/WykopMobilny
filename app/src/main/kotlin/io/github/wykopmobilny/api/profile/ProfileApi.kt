package io.github.wykopmobilny.api.profile

import io.github.wykopmobilny.api.responses.BadgeResponse
import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.api.responses.ProfileResponse
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.EntryLink
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.models.dataclass.Related
import io.reactivex.Single

interface ProfileApi {
    fun getIndex(username: String): Single<ProfileResponse>
    fun getActions(username: String): Single<List<EntryLink>>
    fun getAdded(username: String, page: Int): Single<List<Link>>
    fun getPublished(username: String, page: Int): Single<List<Link>>
    fun getDigged(username: String, page: Int): Single<List<Link>>
    fun getBuried(username: String, page: Int): Single<List<Link>>
    fun getLinkComments(username: String, page: Int): Single<List<LinkComment>>
    fun getEntries(username: String, page: Int): Single<List<Entry>>
    fun getEntriesComments(username: String, page: Int): Single<List<EntryComment>>
    fun getRelated(username: String, page: Int): Single<List<Related>>
    fun getBadges(username: String, page: Int): Single<List<BadgeResponse>>

    fun observe(tag: String): Single<ObserveStateResponse>
    fun unobserve(tag: String): Single<ObserveStateResponse>
    fun block(tag: String): Single<ObserveStateResponse>
    fun unblock(tag: String): Single<ObserveStateResponse>
}
