package io.github.wykopmobilny.domain.blacklist.actions

import io.github.wykopmobilny.api.endpoints.TagRetrofitApi
import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.domain.api.ApiClient
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import javax.inject.Inject

internal class TagsRepository @Inject constructor(
    private val api: ApiClient,
    private val tagsApi: TagRetrofitApi,
    private val blacklistPreferencesApi: BlacklistPreferencesApi,
) {

    suspend fun blockTag(tag: String): ObserveStateResponse {
        val response = api.fetch { tagsApi.block(tag) }
        blacklistPreferencesApi.update { it.copy(tags = it.tags + tag) }

        return response
    }

    suspend fun unblockTag(tag: String): ObserveStateResponse {
        val response = api.fetch { tagsApi.unblock(tag) }

        blacklistPreferencesApi.update { it.copy(tags = it.tags - tag) }

        return response
    }
}
