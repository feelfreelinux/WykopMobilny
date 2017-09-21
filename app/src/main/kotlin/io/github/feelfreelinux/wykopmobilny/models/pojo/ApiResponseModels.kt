package io.github.feelfreelinux.wykopmobilny.models.pojo

import com.squareup.moshi.Json
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed


data class VoteResponse(
        val vote: Int,
        val voters: List<Voter>
)

data class Voter (
        @Json(name="author")

        var author : String,
        @Json(name="author_group")

        var authorGroup : Int,
        @Json(name="author_avatar")

        var authorAvatar : String,
        @Json(name="author_avatar_big")

        var authorAvatarBig : String,
        @Json(name="author_avatar_med")

        var authorAvatarMed : String,
        @Json(name="author_avatar_lo")

        var authorAvatarLo : String,
        @Json(name="author_sex")

        var authorSex : String,
        @Json(name="date")

        var date : String
)


data class CommentResponse(
        @Json(name="id")
        var id : Int,

        @Json(name="author")
        var author : String,

        @Json(name="author_avatar")
        var authorAvatar : String,

        @Json(name="author_avatar_big")

        var authorAvatarBig : String,
        @Json(name="author_avatar_med")

        var authorAvatarMed : String,
        @Json(name="author_avatar_lo")

        var authorAvatarLo : String,
        @Json(name="author_group")

        var authorGroup : Int,
        @Json(name="author_sex")

        var authorSex : String,
        @Json(name="date")

        var date : String,
        @Json(name="body")

        var body : String,
        @Json(name="source")

        var source : String?,
        @Json(name="entry_id")

        var entryId : Int,
        @Json(name="blocked")

        var blocked : Boolean,
        @Json(name="deleted")

        var deleted : Boolean,
        @Json(name="vote_count")

        var voteCount : Int,
        @Json(name="user_vote")

        var userVote : Int,
        @Json(name="voters")

        var voters : List<Voter>,
        @Json(name="embed")

        var embed : Embed?,
        @Json(name="type")

        var type : String,
        @Json(name="app")

        var app : String?,
        @Json(name="violation_url")

        var violationUrl : String?
)


data class EntryResponse(
        @Json(name="id")

        var id : Int,
        @Json(name="author")

        var author : String,
        @Json(name="author_avatar")

        var authorAvatar : String,
        @Json(name="author_avatar_big")

        var authorAvatarBig : String,
        @Json(name="author_avatar_med")

        var authorAvatarMed : String,
        @Json(name="author_avatar_lo")

        var authorAvatarLo : String,
        @Json(name="author_group")

        var authorGroup : Int,
        @Json(name="author_sex")

        var authorSex : String,
        @Json(name="date")

        var date : String,
        @Json(name="body")

        var body : String,
        @Json(name="source")

        var source : String?,
        @Json(name="url")

        var url : String,
        @Json(name="receiver")

        var receiver : String?,
        @Json(name="receiver_avatar")

        var receiverAvatar : String?,
        @Json(name="receiver_avatar_big")

        var receiverAvatarBig : String?,
        @Json(name="receiver_avatar_med")

        var receiverAvatarMed : String?,
        @Json(name="receiver_avatar_lo")

        var receiverAvatarLo : String?,
        @Json(name="receiver_group")

        var receiverGroup : Int?,
        @Json(name="receiver_sex")

        var receiverSex : String?,
        @Json(name="comments")

        var comments : List<CommentResponse>?,
        @Json(name="type")

        var type : String,
        @Json(name="embed")

        var embed : Embed?,
        @Json(name="deleted")

        var deleted : Boolean,
        @Json(name="vote_count")

        var voteCount : Int,
        @Json(name="user_vote")

        var userVote : Int,
        @Json(name="voters")

        var voters : List<Voter>,
        @Json(name="violation_url")

        var violationUrl : String?,
        @Json(name="can_comment")

        var canComment : Boolean?,
        @Json(name="app")

        var app : String?,
        @Json(name="comment_count")

        var commentCount : Int
)


data class TagMetaResponse(
        @Json(name="tag")
        var tag: String,
        @Json(name="is_observed")

        var isObserved: Boolean?,
        @Json(name="is_blocked")

        var isBlocked: Boolean?,
        @Json(name="counters")

        var counters: Any?
)

data class TagEntriesResponse (
        @Json(name="meta")
        var meta: TagMetaResponse,
        @Json(name="items")
        var items: List<EntryResponse>
)

data class NotificationCountResponse(
        @Json(name="count")

        val count: Int
)

class Profile (
        @Json(name = "login")
        var login: String,
        @Json(name="email")

        var email: String,
        @Json(name="public_email")

        var publicEmail: String,
        @Json(name="name")

        var name: String,
        @Json(name="www")

        var www: String,
        @Json(name="jabber")

        var jabber: String,
        @Json(name="gg")

        var gg: String,
        @Json(name="city")

        var city: String,
        @Json(name="about")

        var about: String,
        @Json(name="author_group")

        var authorGroup: Int,
        @Json(name="links_added")

        var linksAdded: Int,
        @Json(name="links_published")

        var linksPublished: Int,
        @Json(name="comments")

        var comments: Int,
        @Json(name="rank")

        var rank: Int,
        @Json(name="followers")

        var followers: Int,
        @Json(name="following")

        var following: Int,
        @Json(name="entries")

        var entries: Int,
        @Json(name="entries_comments")

        var entriesComments: Int,
        @Json(name="diggs")

        var diggs: Int,
        @Json(name="buries")

        var buries: Int,
        @Json(name="groups")

        var groups: Int,
        @Json(name="related_links")

        var relatedLinks: Int,
        @Json(name="signup_date")

        var signupDate: String,
        @Json(name="avatar")

        var avatar: String,
        @Json(name="avatar_big")

        var avatarBig: String,
        @Json(name="avatar_med")

        var avatarMed: String,
        @Json(name="avatar_lo")

        var avatarLo: String,
        @Json(name="is_observed")

        var isObserved: Boolean?,
        @Json(name="is_blocked")

        var isBlocked: Boolean?,
        @Json(name="sex")

        var sex: String,
        @Json(name="url")

        var url: String,
        @Json(name="violation_url")

        var violationUrl: String?,
        @Json(name = "userkey")
        var userKey: String?
)