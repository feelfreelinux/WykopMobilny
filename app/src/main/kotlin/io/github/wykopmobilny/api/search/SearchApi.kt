package io.github.wykopmobilny.api.search

import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.Link
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
