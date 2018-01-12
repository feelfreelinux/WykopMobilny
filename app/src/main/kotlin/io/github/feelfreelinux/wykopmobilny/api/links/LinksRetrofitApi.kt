package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface LinksRetrofitApi {
    @GET("/links/promoted/page/{page}/appkey/$APP_KEY")
    fun getPromoted(@Path("page") page : Int) : Single<WykopApiResponse<List<LinkResponse>>>

    @GET("/links/comments/{linkId}/sort/{sortBy}/appkey/$APP_KEY")
    fun getLinkComments(@Path("linkId") linkId : Int, @Path("sortBy") sortBy : String) : Single<WykopApiResponse<List<LinkCommentResponse>>>

    @GET("/links/commentVoteUp/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteUp(@Path("linkId") linkId : Int) : Single<WykopApiResponse<LinkVoteResponse>>


    @GET("/links/commentVoteDown/1/{linkId}/appkey/$APP_KEY")
    fun commentVoteDown(@Path("linkId") linkId : Int) : Single<WykopApiResponse<LinkVoteResponse>>


    @GET("/links/commentVoteRemove/{linkId}/appkey/$APP_KEY")
    fun commentVoteRemove(@Path("linkId") linkId : Int) : Single<WykopApiResponse<LinkVoteResponse>>
}