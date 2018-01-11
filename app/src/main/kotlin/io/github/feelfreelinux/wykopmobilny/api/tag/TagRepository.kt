package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandler
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.TagEntriesMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagEntriesResponse
import retrofit2.Retrofit

class TagRepository(retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher) : TagApi {
    private val tagApi by lazy { retrofit.create(TagRetrofitApi::class.java) }

    override fun getTagEntries(tag : String, page : Int) = tagApi.getTagEntries(tag, page)
            .retryWhen(userTokenRefresher)
            .flatMap(ErrorHandler<TagEntriesResponse>())
            .map { TagEntriesMapper.map(it)}

    override fun observe(tag : String) = tagApi.observe(tag)
            .retryWhen(userTokenRefresher)
            .compose<TagStateResponse>(ErrorHandlerTransformer())

    override fun unobserve(tag : String) = tagApi.unobserve(tag)
            .retryWhen(userTokenRefresher)
            .compose<TagStateResponse>(ErrorHandlerTransformer())

    override fun block(tag : String) = tagApi.block(tag)
            .retryWhen(userTokenRefresher)
            .compose<TagStateResponse>(ErrorHandlerTransformer())

    override fun unblock(tag : String) = tagApi.unblock(tag)
            .retryWhen(userTokenRefresher)
            .compose<TagStateResponse>(ErrorHandlerTransformer())
}