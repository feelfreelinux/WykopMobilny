package io.github.wykopmobilny.ui.widgets.survey

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.SurveyAnswerItemBinding
import io.github.wykopmobilny.databinding.SurveyListviewBinding
import io.github.wykopmobilny.models.dataclass.Answer
import io.github.wykopmobilny.models.dataclass.Survey
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.usermanager.UserManagerApi

class SurveyWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = SurveyListviewBinding.inflate(layoutInflater, this)

    init {
        this.isVisible = false
    }

    private lateinit var userManager: UserManagerApi
    private lateinit var surveyData: Survey
    lateinit var voteAnswerListener: (Int) -> Unit

    fun setSurvey(survey: Survey, userManagerApi: UserManagerApi) {
        isVisible = true
        deselectRadioExcept(-1)
        surveyData = survey
        binding.surveyQuestion.text = survey.question
        userManager = userManagerApi
        binding.answerItems.removeAllViews()

        survey.answers.forEach {
            val answerView = SurveyAnswerItemBinding.inflate(layoutInflater, null, false)
            answerView.answer.text = it.answer
            binding.answerItems.addView(createAnswerView(it))
        }

        survey.userAnswer?.let { selected ->
            val answerCheck = binding.answerItems.getChildAt(selected - 1).let(SurveyAnswerItemBinding::bind)
            answerCheck.radioButton.isChecked = true
            // Disable click
            binding.answerItems.children
                .map(SurveyAnswerItemBinding::bind)
                .forEach { it.radioButton.isClickable = false }
        }
        setupButtons()
    }

    private fun createAnswerView(answerData: Answer): View {
        val answerView = SurveyAnswerItemBinding.inflate(layoutInflater, null, false)
        answerView.apply {
            answer.text = answerData.answer
            val llp = percentageView.layoutParams as LinearLayout.LayoutParams
            votesCount.text = resources.getString(R.string.votePercentageCount, answerData.percentage.toInt(), answerData.count)
            llp.weight = answerData.percentage.toFloat()
            percentageView.layoutParams = llp
            radioButton.isVisible = userManager.isUserAuthorized()
        }
        return answerView.root
    }

    private fun setupButtons() {
        binding.answerItems.children
            .map(SurveyAnswerItemBinding::bind)
            .map { it.radioButton }
            .forEachIndexed { index, button ->
                button.setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        deselectRadioExcept(index)
                        voteAnswerListener(index + 1)
                    }
                }
            }
    }

    private fun deselectRadioExcept(index: Int) {
        binding.answerItems.children
            .filterIndexed { itemIndex, _ -> itemIndex != index }
            .map(SurveyAnswerItemBinding::bind)
            .map { it.radioButton }
            .forEach { it.isChecked = false }
    }
}
