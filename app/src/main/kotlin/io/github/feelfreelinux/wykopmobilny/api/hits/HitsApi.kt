package io.github.feelfreelinux.wykopmobilny.api.hits

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.reactivex.Single

interface HitsApi {
    fun currentWeek() : Single<List<Link>>
    fun currentDay() : Single<List<Link>>
    fun popular() : Single<List<Link>>
    fun byMonth(year: Int, month : Int) : Single<List<Link>>
    fun byYear(year : Int) : Single<List<Link>>
}