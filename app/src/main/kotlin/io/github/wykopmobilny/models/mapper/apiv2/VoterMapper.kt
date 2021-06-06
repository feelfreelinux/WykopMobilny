package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.VoterResponse
import io.github.wykopmobilny.models.dataclass.Voter
import io.github.wykopmobilny.models.mapper.Mapper

class VoterMapper {
    companion object : Mapper<VoterResponse, Voter> {
        override fun map(value: VoterResponse) =
            Voter(
                AuthorMapper.map(value.author),
                value.date,
                value.voteType
            )
    }
}
