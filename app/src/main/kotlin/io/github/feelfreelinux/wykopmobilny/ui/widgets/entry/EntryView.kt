package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Survey
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter

interface EntryView : BaseView {
    fun markEntryAsRemoved()
    fun markEntryVoted(voteCount : Int)
    fun markEntryUnvoted(voteCount: Int)
    fun markEntryFavorite(isFavorite : Boolean)
    fun showSurvey(surveyData : Survey)
    fun showVoters(voters : List<Voter>)
}