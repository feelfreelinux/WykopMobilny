package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.api.TagFeedEntries
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.userSessionToken
import retrofit2.Call
import retrofit2.Retrofit

interface TagApi {
    fun getTagEntries(tag : String, page : Int) : Call<TagFeedEntries>
}

class TagRepository(retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : TagApi {
    private val tagApi by lazy { retrofit.create(TagRetrofitApi::class.java) }

    override fun getTagEntries(tag : String, page : Int) = tagApi.getTagEntries(tag, page, apiPreferences.userSessionToken)
}