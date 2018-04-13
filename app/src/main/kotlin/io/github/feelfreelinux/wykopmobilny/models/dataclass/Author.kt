package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.os.Parcel
import android.os.Parcelable

class Author(val nick : String,
                             val avatarUrl : String,
                             val group: Int,
                             val sex : String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nick)
        parcel.writeString(avatarUrl)
        parcel.writeInt(group)
        parcel.writeString(sex)
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