package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base

import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface BaseVoteButtonView : BaseView {
    var voteCount : Int
    var isButtonSelected : Boolean
}