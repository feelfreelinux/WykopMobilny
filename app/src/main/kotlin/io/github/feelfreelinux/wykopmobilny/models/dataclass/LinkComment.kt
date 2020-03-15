package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.os.Parcel
import android.os.Parcelable
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate


class LinkComment(
    val id: Int,
    val author: Author,
    val fullDate: String,
    var body: String?,
    val blocked: Boolean,
    val favorite: Boolean,
    var voteCount: Int,
    var voteCountPlus: Int,
    var voteCountMinus: Int,
    var userVote: Int,
    var parentId: Int,
    val canVote: Boolean,
    val linkId: Int,
    var embed: Embed?,
    val app: String?,
    var isCollapsed: Boolean,
    var isParentCollapsed: Boolean,
    var childCommentCount: Int,
    val violationUrl: String,
    var isNsfw: Boolean = false,
    var isBlocked: Boolean = false
) : Parcelable {

    val url = "https://www.wykop.pl/link/$linkId/#comment-$id"

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Author::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readParcelable(Embed::class.java.classLoader),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeParcelable(author, flags)
        parcel.writeString(date)
        parcel.writeString(body)
        parcel.writeByte(if (blocked) 1 else 0)
        parcel.writeByte(if (favorite) 1 else 0)
        parcel.writeInt(voteCount)
        parcel.writeInt(voteCountPlus)
        parcel.writeInt(voteCountMinus)
        parcel.writeInt(userVote)
        parcel.writeInt(parentId)
        parcel.writeByte(if (canVote) 1 else 0)
        parcel.writeInt(linkId)
        parcel.writeParcelable(embed, flags)
        parcel.writeString(app)
        parcel.writeByte(if (isCollapsed) 1 else 0)
        parcel.writeByte(if (isParentCollapsed) 1 else 0)
        parcel.writeInt(childCommentCount)
        parcel.writeString(violationUrl)
        parcel.writeByte(if (isNsfw) 1 else 0)
        parcel.writeByte(if (isBlocked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LinkComment> {
        override fun createFromParcel(parcel: Parcel): LinkComment {
            return LinkComment(parcel)
        }

        override fun newArray(size: Int): Array<LinkComment?> {
            return arrayOfNulls(size)
        }
    }


    val date: String
        get() = this.fullDate.toPrettyDate()
}