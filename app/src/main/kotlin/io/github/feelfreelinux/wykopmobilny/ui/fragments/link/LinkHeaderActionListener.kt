package io.github.feelfreelinux.wykopmobilny.ui.fragments.link

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface LinkHeaderActionListener {
    fun digLink(link: Link)
    fun buryLink(link: Link, reason: Int)
    fun removeVote(link: Link)
    fun markFavorite(link: Link)
}
