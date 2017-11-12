package io.github.feelfreelinux.wykopmobilny.ui.widgets.survey

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Answer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.survey_answer_item.view.*
import kotlinx.android.synthetic.main.survey_listview.view.*
import javax.inject.Inject

class SurveyWidget : ConstraintLayout, SurveyView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var presenter : SurveyPresenter
    @Inject lateinit var userManager : UserManagerApi

    lateinit var surveyData : Survey

    init {
        View.inflate(context, R.layout.survey_listview, this)
        WykopApp.uiInjector.inject(this)
        isVisible = false
        presenter.subscribe(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun setSurvey(survey: Survey, entryId : Int) {
        isVisible = true
        deselectRadioExcept(-1)
        surveyData = survey
        presenter.entryId = entryId
        surveyQuestion.text = survey.question

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
                            presenter.voteAnswer(index + 1)
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

    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
    }
}