package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoterResponse

class VoterMapper {
    companion object : Mapper<VoterResponse, Voter> {
        override fun map(value: VoterResponse): Voter {
            return Voter(
                    AuthorMapper.map(value.author),
                    value.date,
                    value.voteType
            )
        }
    }
}