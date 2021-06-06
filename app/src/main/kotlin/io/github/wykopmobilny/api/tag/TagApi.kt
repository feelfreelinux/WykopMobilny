package io.github.wykopmobilny.api.tag

import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.api.responses.ObservedTagResponse
import io.github.wykopmobilny.models.dataclass.TagEntries
import io.github.wykopmobilny.models.dataclass.TagLinks
import io.reactivex.Single

interface TagApi {
    fun getTagEntries(tag: String, page: Int): Single<TagEntries>
    fun getTagLinks(tag: String, page: Int): Single<TagLinks>

    fun observe(tag: String): Single<ObserveStateResponse>
    fun unobserve(tag: String): Single<ObserveStateResponse>
    fun block(tag: String): Single<ObserveStateResponse>
    fun unblock(tag: String): Single<ObserveStateResponse>
    fun getObservedTags(): Single<List<ObservedTagResponse>>
}
