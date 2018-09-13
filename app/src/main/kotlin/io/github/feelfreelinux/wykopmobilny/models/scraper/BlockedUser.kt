package io.github.feelfreelinux.wykopmobilny.models.scraper

import pl.droidsonroids.jspoon.annotation.Selector

class BlockedUser {
    @Selector("span > b")
    lateinit var nick : String
}