package io.github.feelfreelinux.wykopmobilny.api.profile

import io.github.feelfreelinux.wykopmobilny.models.dataclass.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.reactivex.Single

interface ProfileApi {
    fun getIndex(username: String) : Single<ProfileResponse>
    fun getActions(username : String) : Single<List<EntryLink>>
    fun getAdded(username : String, page : Int) : Single<List<Link>>
    fun getPublished(username : String, page : Int) : Single<List<Link>>
    fun getDigged(username : String, page : Int) : Single<List<Link>>
    fun getBuried(username : String, page : Int) : Single<List<Link>>
    fun getLinkComments(username : String, page : Int) : Single<List<LinkComment>>
    fun getEntries(username : String, page : Int) : Single<List<Entry>>
    fun getEntriesComments(username : String, page : Int) : Single<List<EntryComment>>
    fun getRelated(username : String, page : Int) : Single<List<Related>>
}