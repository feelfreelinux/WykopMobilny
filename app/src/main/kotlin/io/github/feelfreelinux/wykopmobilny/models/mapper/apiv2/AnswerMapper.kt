package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Answer
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AnswerResponse

class AnswerMapper {
    companion object : Mapper<AnswerResponse, Answer> {
        override fun map(value: AnswerResponse) =
            Answer(value.id, value.answer, value.count, value.percentage)
    }
}