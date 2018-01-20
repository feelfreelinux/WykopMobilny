package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

class RelatedResponse (
        val id : Int,
        val url : String,
        val voteCount : Int,
        val author : AuthorResponse,
        val title : String
)