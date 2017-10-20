package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.mapper.tag.TagEntriesMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.BooleanResponse
import io.reactivex.Single
import retrofit2.Retrofit

interface TagApi {
    fun getTagEntries(tag : String, page : Int) : Single<TagEntries>
    fun observe(tag: String): Single<BooleanResponse>
    fun unobserve(tag: String): Single<BooleanResponse>
    fun block(tag: String): Single<BooleanResponse>
    fun unblock(tag: String): Single<BooleanResponse>
}

class TagRepository(retrofit: Retrofit) : TagApi {
    private val tagApi by lazy { retrofit.create(TagRetrofitApi::class.java) }

    override fun getTagEntries(tag : String, page : Int) = tagApi.getTagEntries(tag, page).map { TagEntriesMapper.map(it) }

    override fun observe(tag : String) = tagApi.observe(tag)

    override fun unobserve(tag : String) = tagApi.unobserve(tag)

    override fun block(tag : String) = tagApi.block(tag)

    override fun unblock(tag : String) = tagApi.unblock(tag)
}