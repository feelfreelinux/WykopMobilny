package io.github.feelfreelinux.wykopmobilny.api.addlink

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AddLinkPreviewImage
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.NewLinkResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AddLinkRetrofitApi {
    @FormUrlEncoded
    @POST("addlink/draft/appkey/$APP_KEY")
    fun getDraft(@Field("url") url: String): Single<NewLinkResponse>

    @GET("/addlink/images/key/{key}/appkey/$APP_KEY")
    fun getImages(@Path("key") key: String): Single<WykopApiResponse<List<AddLinkPreviewImage>>>

    @FormUrlEncoded
    @POST("/addlink/add/key/{key}/appkey/$APP_KEY")
    fun publishLink(
        @Path("key") key: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("tags") tags: String,
        @Field("photo") photo: String? = null,
        @Field("url") url: String,
        @Field("plus18") plus18: Int
    ): Single<WykopApiResponse<LinkResponse>>
}
