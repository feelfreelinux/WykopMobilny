package io.github.feelfreelinux.wykopmobilny.models.scraper

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedTag {
    @Selector("a.lcontrast")
    lateinit var tag : String
    @Selector("a.hide", attr = "href")
    lateinit var removeActionUrl : String
}