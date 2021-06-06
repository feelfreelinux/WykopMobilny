package io.github.wykopmobilny.blacklist.api

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedTag {
    @Selector("a.lcontrast")
    lateinit var tag: String
}
