package io.github.feelfreelinux.wykopmobilny.objects

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



data class VoteResponse(
        val vote: Int,
        val voters: Array<Voter>
)

data class Voter (
        @SerializedName("author")
        @Expose
        var author : String,
        @SerializedName("author_group")
        @Expose
        var authorGroup : Int,
        @SerializedName("author_avatar")
        @Expose
        var authorAvatar : String,
        @SerializedName("author_avatar_big")
        @Expose
        var authorAvatarBig : String,
        @SerializedName("author_avatar_med")
        @Expose
        var authorAvatarMed : String,
        @SerializedName("author_avatar_lo")
        @Expose
        var authorAvatarLo : String,
        @SerializedName("author_sex")
        @Expose
        var authorSex : String,
        @SerializedName("date")
        @Expose
        var date : String
)


data class Comment (
        @SerializedName("id")
        @Expose
        var id : Int,
        @SerializedName("author")
        @Expose
        var author : String,
        @SerializedName("author_avatar")
        @Expose
        var authorAvatar : String,
        @SerializedName("author_avatar_big")
        @Expose
        var authorAvatarBig : String,
        @SerializedName("author_avatar_med")
        @Expose
        var authorAvatarMed : String,
        @SerializedName("author_avatar_lo")
        @Expose
        var authorAvatarLo : String,
        @SerializedName("author_group")
        @Expose
        var authorGroup : Int,
        @SerializedName("author_sex")
        @Expose
        var authorSex : String,
        @SerializedName("date")
        @Expose
        var date : String,
        @SerializedName("body")
        @Expose
        var body : String,
        @SerializedName("source")
        @Expose
        var source : String?,
        @SerializedName("entry_id")
        @Expose
        var entryId : Int,
        @SerializedName("blocked")
        @Expose
        var blocked : Boolean,
        @SerializedName("deleted")
        @Expose
        var deleted : Boolean,
        @SerializedName("vote_count")
        @Expose
        var voteCount : Int,
        @SerializedName("user_vote")
        @Expose
        var userVote : Int,
        @SerializedName("voters")
        @Expose
        var voters : List<Voter>,
        @SerializedName("embed")
        @Expose
        var embed : Embed?,
        @SerializedName("type")
        @Expose
        var type : String,
        @SerializedName("app")
        @Expose
        var app : String?,
        @SerializedName("violation_url")
        @Expose
        var violationUrl : String?
)


data class Entry (
        @SerializedName("id")
        @Expose
        var id : Int,
        @SerializedName("author")
        @Expose
        var author : String,
        @SerializedName("author_avatar")
        @Expose
        var authorAvatar : String,
        @SerializedName("author_avatar_big")
        @Expose
        var authorAvatarBig : String,
        @SerializedName("author_avatar_med")
        @Expose
        var authorAvatarMed : String,
        @SerializedName("author_avatar_lo")
        @Expose
        var authorAvatarLo : String,
        @SerializedName("author_group")
        @Expose
        var authorGroup : Int,
        @SerializedName("author_sex")
        @Expose
        var authorSex : String,
        @SerializedName("date")
        @Expose
        var date : String,
        @SerializedName("body")
        @Expose
        var body : String,
        @SerializedName("source")
        @Expose
        var source : String?,
        @SerializedName("url")
        @Expose
        var url : String,
        @SerializedName("receiver")
        @Expose
        var receiver : String?,
        @SerializedName("receiver_avatar")
        @Expose
        var receiverAvatar : String?,
        @SerializedName("receiver_avatar_big")
        @Expose
        var receiverAvatarBig : String?,
        @SerializedName("receiver_avatar_med")
        @Expose
        var receiverAvatarMed : String?,
        @SerializedName("receiver_avatar_lo")
        @Expose
        var receiverAvatarLo : String?,
        @SerializedName("receiver_group")
        @Expose
        var receiverGroup : Int?,
        @SerializedName("receiver_sex")
        @Expose
        var receiverSex : String?,
        @SerializedName("comments")
        @Expose
        var comments : Array<Comment>?,
        @SerializedName("type")
        @Expose
        var type : String,
        @SerializedName("embed")
        @Expose
        var embed : Embed?,
        @SerializedName("deleted")
        @Expose
        var deleted : Boolean,
        @SerializedName("vote_count")
        @Expose
        var voteCount : Int,
        @SerializedName("user_vote")
        @Expose
        var userVote : Int,
        @SerializedName("voters")
        @Expose
        var voters : Array<Voter>,
        @SerializedName("violation_url")
        @Expose
        var violationUrl : String?,
        @SerializedName("can_comment")
        @Expose
        var canComment : Boolean,
        @SerializedName("app")
        @Expose
        var app : String?,
        @SerializedName("comment_count")
        @Expose
        var commentCount : Int
)


data class TagMeta(
        @SerializedName("tag")
        @Expose
        var tag: String,
        @SerializedName("is_observed")
        @Expose
        var isObserved: Boolean?,
        @SerializedName("is_blocked")
        @Expose
        var isBlocked: Boolean?,
        @SerializedName("counters")
        @Expose
        var counters: Any?
)

data class TagFeedEntries (
        @SerializedName("counters")
        @Expose
        var meta: TagMeta,
        @SerializedName("items")
        @Expose
        var items: Array<Entry>
)

data class NotificationCountResponse(
        @SerializedName("count")
        @Expose
        val count: Int
)

class Profile (
        @SerializedName("login")
        @Expose
        var login: String,
        @SerializedName("email")
        @Expose
        var email: String,
        @SerializedName("public_email")
        @Expose
        var publicEmail: String,
        @SerializedName("name")
        @Expose
        var name: String,
        @SerializedName("www")
        @Expose
        var www: String,
        @SerializedName("jabber")
        @Expose
        var jabber: String,
        @SerializedName("gg")
        @Expose
        var gg: String,
        @SerializedName("city")
        @Expose
        var city: String,
        @SerializedName("about")
        @Expose
        var about: String,
        @SerializedName("author_group")
        @Expose
        var authorGroup: Int,
        @SerializedName("links_added")
        @Expose
        var linksAdded: Int,
        @SerializedName("links_published")
        @Expose
        var linksPublished: Int,
        @SerializedName("comments")
        @Expose
        var comments: Int,
        @SerializedName("rank")
        @Expose
        var rank: Int,
        @SerializedName("followers")
        @Expose
        var followers: Int,
        @SerializedName("following")
        @Expose
        var following: Int,
        @SerializedName("entries")
        @Expose
        var entries: Int,
        @SerializedName("entries_comments")
        @Expose
        var entriesComments: Int,
        @SerializedName("diggs")
        @Expose
        var diggs: Int,
        @SerializedName("buries")
        @Expose
        var buries: Int,
        @SerializedName("groups")
        @Expose
        var groups: Int,
        @SerializedName("related_links")
        @Expose
        var relatedLinks: Int,
        @SerializedName("signup_date")
        @Expose
        var signupDate: String,
        @SerializedName("avatar")
        @Expose
        var avatar: String,
        @SerializedName("avatar_big")
        @Expose
        var avatarBig: String,
        @SerializedName("avatar_med")
        @Expose
        var avatarMed: String,
        @SerializedName("avatar_lo")
        @Expose
        var avatarLo: String,
        @SerializedName("is_observed")
        @Expose
        var isObserved: Boolean?,
        @SerializedName("is_blocked")
        @Expose
        var isBlocked: Boolean?,
        @SerializedName("sex")
        @Expose
        var sex: String,
        @SerializedName("url")
        @Expose
        var url: String,
        @SerializedName("violation_url")
        @Expose
        var violationUrl: String?,
        @SerializedName("userkey")
        @Expose
        var userKey: String?
)