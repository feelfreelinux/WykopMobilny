package io.github.feelfreelinux.wykopmobilny.models.fragments

data class PagedDataModel<out T : Any> (val page : Int, val model : T)