package io.github.feelfreelinux.wykopmobilny.objects

import com.google.gson.annotations.SerializedName


data class EntryDetailsResponse(
        val id: Int,
        val author: String,
        val author_avatar: String,
        val author_avatar_big: String,
        val author_avatar_med: String,
        val author_avatar_lo: String,
        val author_group: Int,
        val author_sex: String,
        val date: String,
        val body: String,
        val source: Any,
        val url: String,
        val receiver: Any,
        val receiver_avatar: Any,
        val receiver_avatar_big: Any,
        val receiver_avatar_med: Any,
        val receiver_avatar_lo: Any,
        val receiver_group: Any,
        val receiver_sex: Any,
        val blocked: Boolean,
        val vote_count: Int,
        val user_vote: Int,
        val user_favorite: Boolean,
        val type: String,
        val embed: Any,
        val deleted: Boolean,
        val violation_url: String,
        val can_comment: Boolean,
        val app: Any,
        val comment_count: Int,
        val voters: List<Voter>,
        val comments: List<Comment>
)

data class Comment(
        val id: Int,
        val author: String,
        val author_avatar: String,
        val author_avatar_big: String,
        val author_avatar_med: String,
        val author_avatar_lo: String,
        val author_group: Int,
        val author_sex: String,
        val date: String,
        val body: String,
        val source: Any,
        val entry_id: Int,
        val blocked: Boolean,
        val deleted: Boolean,
        val vote_count: Int,
        val user_vote: Int,
        val voters: List<Voter>,
        val embed: Any,
        val type: String,
        val app: Any,
        val violation_url: String

)