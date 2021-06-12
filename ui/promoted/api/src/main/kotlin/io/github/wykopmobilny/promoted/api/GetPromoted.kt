package io.github.wykopmobilny.promoted.api

import androidx.paging.PagingData
import io.github.wykopmobilny.ui.base.Query

interface GetPromoted : Query<PagingData<Link>> {
}
