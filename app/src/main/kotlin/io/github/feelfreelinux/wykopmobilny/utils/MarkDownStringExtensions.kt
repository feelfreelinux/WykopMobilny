package io.github.feelfreelinux.wykopmobilny.utils

fun String.markdownLink(description: String): String = "[$description](${this})"
