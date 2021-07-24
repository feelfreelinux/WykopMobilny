package io.github.wykopmobilny.blacklist.api

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedTags {

    @Selector("div.tagcard")
    var tags: List<BlockedTag>? = null
}
