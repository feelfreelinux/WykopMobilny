package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.mapper.tag.TagEntriesMapper
import io.reactivex.Single
import retrofit2.Retrofit

interface TagApi {
    fun getTagEntries(tag : String, page : Int) : Single<TagEntries>
    fun observe(tag: String): Single<Boolean>
    fun unobserve(tag: String): Single<Boolean>
    fun block(tag: String): Single<Boolean>
    fun unblock(tag: String): Single<Boolean>
}

class TagRepository(retrofit: Retrofit) : TagApi {
    private val tagApi by lazy { retrofit.create(TagRetrofitApi::class.java) }

    override fun getTagEntries(tag : String, page : Int) = tagApi.getTagEntries(tag, page).map { TagEntriesMapper.map(it) }

    override fun observe(tag : String) = tagApi.observe(tag).map { it.first() }

    override fun unobserve(tag : String) = tagApi.unobserve(tag).map { it.first() }

    override fun block(tag : String) = tagApi.block(tag).map { it.first() }

    override fun unblock(tag : String) = tagApi.unblock(tag).map { it.first() }
}