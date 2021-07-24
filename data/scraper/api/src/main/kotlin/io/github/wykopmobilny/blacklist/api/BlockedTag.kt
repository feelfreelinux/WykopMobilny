package io.github.wykopmobilny.blacklist.api

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedTag {

    @Selector("a.lcontrast")
    var tag: String? = null
}
