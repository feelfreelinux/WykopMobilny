package io.github.feelfreelinux.wykopmobilny.ui.widgets.survey

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Answer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.survey_answer_item.view.*
import kotlinx.android.synthetic.main.survey_listview.view.*

class SurveyWidget : androidx.constraintlayout.widget.ConstraintLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var userManager : UserManagerApi

    lateinit var surveyData : Survey

    lateinit var voteAnswerListener : (Int) -> Unit

    init {
        View.inflate(context, R.layout.survey_listview, this)
        isVisible = false
    }



    fun setSurvey(survey: Survey, userManagerApi: UserManagerApi) {
        isVisible = true
        deselectRadioExcept(-1)
        surveyData = survey
        surveyQuestion.text = survey.question
        userManager = userManagerApi
        answerItems.removeAllViews()

        survey.answers.forEach {
            val answerView = View.inflate(context, R.layout.survey_answer_item, null)
            answerView.answer.text = it.answer
            answerItems.addView(createAnswerView(it))
        }

        survey.userAnswer?.apply {
            val answerCheck = answerItems.getChildAt(this - 1).radioButton
            answerCheck.isChecked = true
            // Disable click
            (0 until answerItems.childCount)
                    .map { answerItems.getChildAt(it).radioButton}
                    .forEach { it.isClickable = false }
        }
        setupButtons()
    }

    private fun createAnswerView(answerData: Answer) : View {
        val answerView = View.inflate(context, R.layout.survey_answer_item, null)
        answerView.apply {
            answer.text = answerData.answer
            val llp = percentage_view.layoutParams as LinearLayout.LayoutParams
            votesCount.text = resources.getString(R.string.votePercentageCount, answerData.percentage.toInt(), answerData.count)
            llp.weight = answerData.percentage.toFloat()
            percentage_view.layoutParams = llp
            radioButton.isVisible = userManager.isUserAuthorized()
        }
        return answerView
    }

    private fun setupButtons() {
        (0 until answerItems.childCount)
                .map { answerItems.getChildAt(it).radioButton}
                .forEachIndexed {
                    index, button ->
                    button.setOnCheckedChangeListener { _, checked ->
                        if (checked) {
                            deselectRadioExcept(index)
                            voteAnswerListener(index + 1)
                        }
                    }
                }
    }

    private fun deselectRadioExcept(index : Int) {
        (0 until answerItems.childCount)
                .filter { it != index }
                .map { answerItems.getChildAt(it).radioButton}
                .forEach { it.isChecked = false }
    }

}