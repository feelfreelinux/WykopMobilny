package io.github.feelfreelinux.wykopmobilny.ui.fragments.links

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface LinkActionListener {
    fun dig(link : Link)
    fun removeVote(link: Link)
}