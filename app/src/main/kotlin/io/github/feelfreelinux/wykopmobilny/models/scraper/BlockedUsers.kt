package io.github.feelfreelinux.wykopmobilny.models.scraper

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedUsers {
    @Selector("div.usercard")
    var blockedUsers: List<BlockedUser>? = null
}