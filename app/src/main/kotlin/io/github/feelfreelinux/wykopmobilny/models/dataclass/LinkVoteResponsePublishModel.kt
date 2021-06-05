package io.github.feelfreelinux.wykopmobilny.models.dataclass

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse

data class LinkVoteResponsePublishModel(
    val linkId: Int,
    val voteResponse: DigResponse
)
