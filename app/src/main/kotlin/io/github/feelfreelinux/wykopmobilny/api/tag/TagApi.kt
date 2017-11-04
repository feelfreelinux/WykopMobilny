package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagEntriesResponse
import io.reactivex.Single

interface TagApi {
    fun getTagEntries(tag : String, page : Int) : Single<TagEntries>
    fun observe(tag: String): Single<TagStateResponse>
    fun unobserve(tag: String): Single<TagStateResponse>
    fun block(tag: String): Single<TagStateResponse>
    fun unblock(tag: String): Single<TagStateResponse>
}