package io.github.feelfreelinux.wykopmobilny.utils

val String.markdownBold : String get() = "**${this}**"

val String.markdownItalic : String get() = "_${this}_"

val String.markdownQuote : String get() = "\n> ${this}"

fun String.markdownLink(description : String): String = "[$description](${this})"

val String.markdownSourceCode : String get() = "`${this}`"

val String.markdownSpoiler : String get() = "\n! ${this}"
