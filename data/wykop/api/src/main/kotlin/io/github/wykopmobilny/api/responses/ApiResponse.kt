package io.github.wykopmobilny.api.responses

interface ApiResponse<out T> {
    val data: T?
    val error: WykopErrorResponse?
}
