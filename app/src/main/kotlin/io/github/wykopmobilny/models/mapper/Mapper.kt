package io.github.wykopmobilny.models.mapper

interface Mapper<in T, out Y> {
    fun map(value: T): Y
}
