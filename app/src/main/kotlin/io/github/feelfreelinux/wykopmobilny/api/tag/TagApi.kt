package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagLinks
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.reactivex.Single

interface TagApi {
    fun getTagEntries(tag : String, page : Int) : Single<TagEntries>
    fun getTagLinks(tag : String, page : Int) : Single<TagLinks>

    fun observe(tag: String): Single<TagStateResponse>
    fun unobserve(tag: String): Single<TagStateResponse>
    fun block(tag: String): Single<TagStateResponse>
    fun unblock(tag: String): Single<TagStateResponse>
    fun getObservedTags(): Single<List<ObservedTagResponse>>

}