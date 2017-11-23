package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.AutoCompleteTextView


class DelayAutoCompleteTextView: AppCompatAutoCompleteTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var autoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY

    private val mHandler = Handler {
        performFiltering(it.obj as CharSequence, it.arg1)
        true
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        mHandler.removeMessages(100)
        mHandler.sendMessageDelayed(mHandler.obtainMessage(100, text), 750)
    }

    companion object {

        private val MESSAGE_TEXT_CHANGED = 100
        private val DEFAULT_AUTOCOMPLETE_DELAY = 750
    }
}