package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.TagEntriesMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagEntriesResponse
import retrofit2.Retrofit

class TagRepository(retrofit: Retrofit) : TagApi {
    private val tagApi by lazy { retrofit.create(TagRetrofitApi::class.java) }

    override fun getTagEntries(tag : String, page : Int) = tagApi.getTagEntries(tag, page)
            .flatMap(ErrorHandler<TagEntriesResponse>())
            .map { TagEntriesMapper.map(it)}

    override fun observe(tag : String) = tagApi.observe(tag)
            .compose<TagStateResponse>(ErrorHandlerTransformer())

    override fun unobserve(tag : String) = tagApi.unobserve(tag)
            .compose<TagStateResponse>(ErrorHandlerTransformer())

    override fun block(tag : String) = tagApi.block(tag)
            .compose<TagStateResponse>(ErrorHandlerTransformer())

    override fun unblock(tag : String) = tagApi.unblock(tag)
            .compose<TagStateResponse>(ErrorHandlerTransformer())
}