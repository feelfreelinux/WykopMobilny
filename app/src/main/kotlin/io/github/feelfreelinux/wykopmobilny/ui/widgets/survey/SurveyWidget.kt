package io.github.feelfreelinux.wykopmobilny.ui.widgets.survey

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Answer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.survey_answer_item.view.*
import kotlinx.android.synthetic.main.survey_listview.view.*

class SurveyWidget : ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var surveyData : Survey

    init {
        View.inflate(context, R.layout.survey_listview, this)
    }

    fun setSurvey(survey: Survey) {
        isVisible = true
        surveyData = survey
        surveyQuestion.text = survey.question

        answerItems.removeAllViews()
        for (answer in survey.answers) {
            val answerView = View.inflate(context, R.layout.survey_answer_item, null)
            answerView.answer.text = answer.answer
            answerItems.addView(createAnswerView(answer))
        }
    }

    private fun createAnswerView(answerData: Answer) : View {
        val answerView = View.inflate(context, R.layout.survey_answer_item, null)
        answerView.apply {
            answer.text = answerData.answer
            val llp = percentage_view.layoutParams as LinearLayout.LayoutParams
            votesCount.text = "${answerData.percentage.toInt()}% (${answerData.count} głosów)"
            llp.weight = 100 - answerData.percentage.toFloat()
            percentage_view.layoutParams = llp
        }
        return answerView
    }
}