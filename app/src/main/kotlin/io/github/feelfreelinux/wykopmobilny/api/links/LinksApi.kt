package io.github.feelfreelinux.wykopmobilny.api.links

import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.reactivex.Single

interface LinksApi {
    fun getPromoted(page : Int) : Single<List<Link>>
    fun getLinkComments(linkId: Int, sortBy : String) : Single<List<LinkComment>>
    fun getLink(linkId: Int) : Single<Link>

    fun commentVoteUp(linkId: Int) : Single<LinkVoteResponse>
    fun commentVoteDown(linkId: Int) : Single<LinkVoteResponse>
    fun commentVoteCancel(linkId: Int) : Single<LinkVoteResponse>
    fun commentDelete(commentId: Int) : Single<LinkComment>
    fun commentAdd(body : String, inputStream: TypedInputStream,
                   linkId: Int, linkComment: Int) : Single<LinkComment>
    fun commentAdd(body : String, embed: String?,
                   linkId: Int, linkComment: Int) : Single<LinkComment>
    fun commentAdd(body : String, inputStream: TypedInputStream,
                   linkId: Int) : Single<LinkComment>
    fun commentAdd(body : String, embed: String?,
                   linkId: Int) : Single<LinkComment>
    fun voteUp(linkId: Int) : Single<DigResponse>
    fun voteDown(linkId: Int, reason : Int) : Single<DigResponse>
    fun voteRemove(linkId: Int) : Single<DigResponse>
}