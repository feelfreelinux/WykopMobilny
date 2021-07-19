package io.github.wykopmobilny.domain.blacklist.actions

import io.github.wykopmobilny.api.endpoints.TagRetrofitApi
import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.domain.api.ApiClient
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import javax.inject.Inject

internal class UsersRepository @Inject constructor(
    private val api: ApiClient,
    private val usersApi: TagRetrofitApi,
    private val blacklistPreferencesApi: BlacklistPreferencesApi,
) {

    suspend fun blockUser(user: String): ObserveStateResponse {
        val response = api.fetch { usersApi.block(user) }
        blacklistPreferencesApi.update { it.copy(users = it.users + user) }

        return response
    }

    suspend fun unblockUser(user: String): ObserveStateResponse {
        val response = api.fetch { usersApi.unblock(user) }
        blacklistPreferencesApi.update { it.copy(users = it.users - user) }

        return response
    }
}
