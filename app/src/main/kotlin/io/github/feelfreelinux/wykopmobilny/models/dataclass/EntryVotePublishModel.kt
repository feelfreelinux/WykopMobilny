package io.github.feelfreelinux.wykopmobilny.models.dataclass

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse

data class EntryVotePublishModel(
    val entryId: Int,
    val voteResponse: VoteResponse
)
