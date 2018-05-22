package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


class Link(
        val id: Int,
        val title: String,
        val description: String,
        val tags: String,
        val sourceUrl: String,
        var voteCount: Int,
        var buryCount: Int,
        var comments: List<LinkComment>,
        val commentsCount: Int,
        val relatedCount: Int,
        val author: Author?,
        val date: String,
        val preview: String?,
        val plus18: Boolean,
        val canVote: Boolean,
        val isHot: Boolean,
        val status: String,
        var userVote: String?,
        var userFavorite: Boolean,
        val app: String?,
        var gotSelected : Boolean,
        var isBlocked : Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createTypedArrayList(LinkComment.CREATOR),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readParcelable(Author::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(tags)
        parcel.writeString(sourceUrl)
        parcel.writeInt(voteCount)
        parcel.writeInt(buryCount)
        parcel.writeTypedList(comments)
        parcel.writeInt(commentsCount)
        parcel.writeInt(relatedCount)
        parcel.writeParcelable(author, flags)
        parcel.writeString(date)
        parcel.writeString(preview)
        parcel.writeByte(if (plus18) 1 else 0)
        parcel.writeByte(if (canVote) 1 else 0)
        parcel.writeByte(if (isHot) 1 else 0)
        parcel.writeString(status)
        parcel.writeString(userVote)
        parcel.writeByte(if (userFavorite) 1 else 0)
        parcel.writeString(app)
        parcel.writeByte(if (gotSelected) 1 else 0)
        parcel.writeByte(if (isBlocked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Link> {
        override fun createFromParcel(parcel: Parcel): Link {
            return Link(parcel)
        }

        override fun newArray(size: Int): Array<Link?> {
            return arrayOfNulls(size)
        }
    }

    val url = "https://www.wykop.pl/link/$id"
}