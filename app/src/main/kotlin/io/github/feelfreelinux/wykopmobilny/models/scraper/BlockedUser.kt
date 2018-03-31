package io.github.feelfreelinux.wykopmobilny.models.scraper

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedUser {
    @Selector("span > b")
    lateinit var nick : String
    @Selector("img.avatar", attr = "src")
    lateinit var avatarUrl : String
    @Selector("a.hide", attr = "ref")
    lateinit var removeActionUrl : String
}