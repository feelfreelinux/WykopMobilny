package io.github.wykopmobilny.models.dataclass

import android.os.Parcel
import android.os.Parcelable

class Embed(
    val type: String,
    val preview: String,
    val url: String,
    val plus18: Boolean,
    val source: String?,
    val isAnimated: Boolean,
    val size: String,
    var isResize: Boolean = false,
    var isRevealed: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(preview)
        parcel.writeString(url)
        parcel.writeByte(if (plus18) 1 else 0)
        parcel.writeString(source)
        parcel.writeByte(if (isAnimated) 1 else 0)
        parcel.writeString(size)
        parcel.writeByte(if (isResize) 1 else 0)
        parcel.writeByte(if (isRevealed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Embed> {
        override fun createFromParcel(parcel: Parcel): Embed {
            return Embed(parcel)
        }

        override fun newArray(size: Int): Array<Embed?> {
            return arrayOfNulls(size)
        }
    }
}
