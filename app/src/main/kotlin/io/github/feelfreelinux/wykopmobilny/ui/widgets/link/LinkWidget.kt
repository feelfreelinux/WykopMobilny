package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext

class LinkWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    init {
        View.inflate(context, R.layout.link_details_header_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun setEntryData() {

    }

    private fun setupHeader() {
    }

    private fun setupButtons() {
    }

    private fun setupBody() {
    }

}