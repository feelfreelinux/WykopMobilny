package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class EntriesFragmentPresenter(val schedulers : Schedulers, val entriesApi: EntriesApi) : BasePresenter<EntriesFragmentView>() {

}