package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common

interface ApiResponse<out T> {
    val data : T?
    val error : WykopErrorResponse?
}