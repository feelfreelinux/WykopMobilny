package io.github.feelfreelinux.wykopmobilny.models.scraper

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedUsers {
    @Selector("div.usercard")
    lateinit var blockedUsers : List<BlockedUser>
}