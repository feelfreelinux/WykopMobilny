package io.github.feelfreelinux.wykopmobilny.models.mapper

interface Mapper<in T, out V> {
    fun map(value : T) : V
}