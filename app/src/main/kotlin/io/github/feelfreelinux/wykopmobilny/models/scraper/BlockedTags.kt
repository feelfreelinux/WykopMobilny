package io.github.feelfreelinux.wykopmobilny.models.scraper

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedTags {
    @Selector("div.tagcard")
    var blockedTags: List<BlockedTag>? = null
}
