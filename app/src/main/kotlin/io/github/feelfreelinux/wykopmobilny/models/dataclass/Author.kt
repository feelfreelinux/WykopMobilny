package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons.PatronBadge
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser

class Author(
    val nick: String,
    val avatarUrl: String,
    val group: Int,
    val sex: String,
    var badge: PatronBadge? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readParcelable(PatronBadge::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nick)
        parcel.writeString(avatarUrl)
        parcel.writeInt(group)
        parcel.writeString(sex)
        parcel.writeParcelable(badge, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Author> {
        override fun createFromParcel(parcel: Parcel): Author {
            return Author(parcel)
        }

        override fun newArray(size: Int): Array<Author?> {
            return arrayOfNulls(size)
        }
    }
}

fun PatronBadge.drawBadge(view: TextView) {
    val shape = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor(hexColor), Color.parseColor(hexColor)))
    view.setOnClickListener {
        view.getActivityContext()?.openBrowser("https://patronite.pl/wykop-mobilny")
    }
    shape.cornerRadius = 30f
    view.text = text
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        view.background = shape
    } else {
        view.setBackgroundDrawable(shape)
    }
}