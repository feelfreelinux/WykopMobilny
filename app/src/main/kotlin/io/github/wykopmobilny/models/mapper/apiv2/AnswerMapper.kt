package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.AnswerResponse
import io.github.wykopmobilny.models.dataclass.Answer
import io.github.wykopmobilny.models.mapper.Mapper

object AnswerMapper : Mapper<AnswerResponse, Answer> {
    override fun map(value: AnswerResponse) =
        Answer(value.id, value.answer, value.count, value.percentage)
}
