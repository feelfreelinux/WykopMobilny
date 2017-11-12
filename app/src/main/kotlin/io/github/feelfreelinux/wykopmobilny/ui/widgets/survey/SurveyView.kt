package io.github.feelfreelinux.wykopmobilny.ui.widgets.survey

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Answer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey

interface SurveyView : BaseView {
    fun setSurvey(survey : Survey, entryId : Int)
}