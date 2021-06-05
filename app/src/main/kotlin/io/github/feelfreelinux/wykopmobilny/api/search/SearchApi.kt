package io.github.feelfreelinux.wykopmobilny.api.search

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.reactivex.Single

interface SearchApi {
    fun searchLinks(
        page: Int,
        query: String
    ): Single<List<Link>>

    fun searchEntries(
        page: Int,
        query: String
    ): Single<List<Entry>>

    fun searchProfiles(query: String): Single<List<Author>>
}
