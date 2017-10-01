package io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.vote.base

import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface BaseVoteButtonView : BaseView {
    var voteCount : Int
    var isButtonSelected : Boolean
}