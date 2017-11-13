package io.github.feelfreelinux.wykopmobilny.models.dataclass

import com.squareup.moshi.Json
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse

data class Voter(
        val author : Author,
        val date : String,
        val voteType : Int
)