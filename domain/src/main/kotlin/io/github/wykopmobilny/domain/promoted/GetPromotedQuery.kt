package io.github.wykopmobilny.domain.promoted

import androidx.paging.Pager
import io.github.wykopmobilny.promoted.api.GetPromoted
import io.github.wykopmobilny.promoted.api.Link
import javax.inject.Inject

class GetPromotedQuery @Inject constructor(
    private val pager: Pager<Int, Link>,
) : GetPromoted {

    override fun invoke() = pager.flow
}
