package io.github.wykopmobilny.blacklist.api

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedUser {

    @Selector("span > b")
    var nick: String? = null
}
