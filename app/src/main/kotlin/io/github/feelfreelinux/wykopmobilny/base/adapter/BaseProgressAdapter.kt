package io.github.feelfreelinux.wykopmobilny.base.adapter
interface BaseProgressAdapter<T : Any> {
    fun disableLoading()
    fun addData(items : List<T>, shouldClearAdapter : Boolean)
    val data : List<T>
    fun setHasStableIds(hasStableIds : Boolean)
}