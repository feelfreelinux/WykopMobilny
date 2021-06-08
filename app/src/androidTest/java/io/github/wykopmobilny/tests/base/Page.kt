package io.github.wykopmobilny.tests.base

interface Page

operator fun <T : Page> T.invoke(block: T.() -> Unit) = block(this)
