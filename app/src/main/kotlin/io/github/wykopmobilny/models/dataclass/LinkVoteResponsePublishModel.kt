package io.github.wykopmobilny.models.dataclass

import io.github.wykopmobilny.api.responses.DigResponse

data class LinkVoteResponsePublishModel(
    val linkId: Int,
    val voteResponse: DigResponse
)
