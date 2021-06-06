package io.github.wykopmobilny.blacklist.api

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedUsers {
    @Selector("div.usercard")
    var users: List<BlockedUser>? = null
}
