package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.reactivex.Single

interface MyWykopApi {
    fun getIndex(page: Int): Single<List<EntryLink>>
    fun byTags(page: Int): Single<List<EntryLink>>
    fun byUsers(page: Int): Single<List<EntryLink>>
}
