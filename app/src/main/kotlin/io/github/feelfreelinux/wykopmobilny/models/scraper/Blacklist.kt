package io.github.feelfreelinux.wykopmobilny.models.scraper

import pl.droidsonroids.jspoon.annotation.Selector


class Blacklist {
    @Selector(value = "div.space[data-type=users]")
    lateinit var users : BlockedUsers

    @Selector(value = "div.space[data-type=hashtags]")
    lateinit var tags : BlockedTags
}