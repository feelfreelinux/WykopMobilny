package io.github.feelfreelinux.wykopmobilny.models.fragments

data class SortedPagedDataModel<out T : Any> (val page : Int, val sortby : String, val model : T)