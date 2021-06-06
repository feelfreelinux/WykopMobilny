package io.github.wykopmobilny.blacklist.api

import pl.droidsonroids.jspoon.annotation.Selector

class Blacklist {
    @Selector(value = "div.space[data-type=users]")
    var users: BlockedUsers? = null

    @Selector(value = "div.space[data-type=hashtags]")
    var tags: BlockedTags? = null
}
