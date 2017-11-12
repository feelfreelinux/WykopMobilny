package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.SurveyResponse

class SurveyMapper {
    companion object : Mapper<SurveyResponse, Survey> {
        override fun map(value: SurveyResponse): Survey {
            return Survey(value.question,
                    value.answers.map { AnswerMapper.map(it) },
                    value.userAnswer)
        }
    }
}