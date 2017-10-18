package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.mapper.tag.TagEntriesMapper
import io.reactivex.Single
import retrofit2.Retrofit

interface TagApi {
    fun getTagEntries(tag : String, page : Int) : Single<TagEntries>
}

class TagRepository(retrofit: Retrofit) : TagApi {
    private val tagApi by lazy { retrofit.create(TagRetrofitApi::class.java) }

    override fun getTagEntries(tag : String, page : Int) = tagApi.getTagEntries(tag, page).map { TagEntriesMapper.map(it) }
}