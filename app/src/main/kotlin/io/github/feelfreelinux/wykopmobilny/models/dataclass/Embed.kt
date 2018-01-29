package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.os.Parcel
import android.os.Parcelable

class Embed(val type: String,
            val preview: String,
            val url: String,
            val plus18: Boolean,
            val source: String?,
            val isAnimated: Boolean,
            val size: String,
            var isResize: Boolean = false,
            var isRevealed: Boolean = false) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readString(),
            1 == source.readInt(),
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(type)
        writeString(preview)
        writeString(url)
        writeInt((if (plus18) 1 else 0))
        writeString(source)
        writeInt((if (isAnimated) 1 else 0))
        writeString(size)
        writeInt((if (isResize) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Embed> = object : Parcelable.Creator<Embed> {
            override fun createFromParcel(source: Parcel): Embed = Embed(source)
            override fun newArray(size: Int): Array<Embed?> = arrayOfNulls(size)
        }
    }
}