package io.github.wykopmobilny.ui.fragments.link

import io.github.wykopmobilny.models.dataclass.Link

interface LinkHeaderActionListener {
    fun digLink(link: Link)
    fun buryLink(link: Link, reason: Int)
    fun removeVote(link: Link)
    fun markFavorite(link: Link)
}
