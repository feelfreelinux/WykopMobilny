package io.github.feelfreelinux.wykopmobilny.ui.widgets.survey

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class SurveyPresenter(val subscriptionHelper: SubscriptionHelperApi, val entriesApi: EntriesApi) : BasePresenter<SurveyView>() {
    var entryId = 0
    fun voteAnswer(answerId : Int) {
        subscriptionHelper.subscribe(entriesApi.voteSurvey(entryId, answerId),
                { view?.setSurvey(it, entryId) },
                { view?.showErrorDialog(it) },
                this)
    }
}