package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


class LinkComment(
        val id: Int,
        val author: Author,
        val date: String,
        val body: String?,
        val blocked: Boolean,
        val favorite: Boolean,
        var voteCount: Int,
        var voteCountPlus: Int,
        var voteCountMinus: Int,
        var userVote: Int,
        val parentId: Int,
        val canVote: Boolean,
        val linkId: Int,
        val embed: Embed?,
        val app: String?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readParcelable<Author>(Author::class.java.classLoader),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            1 == source.readInt(),
            source.readInt(),
            source.readParcelable<Embed>(Embed::class.java.classLoader),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeParcelable(author, 0)
        writeString(date)
        writeString(body)
        writeInt((if (blocked) 1 else 0))
        writeInt((if (favorite) 1 else 0))
        writeInt(voteCount)
        writeInt(voteCountPlus)
        writeInt(voteCountMinus)
        writeInt(userVote)
        writeInt(parentId)
        writeInt((if (canVote) 1 else 0))
        writeInt(linkId)
        writeParcelable(embed, 0)
        writeString(app)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LinkComment> = object : Parcelable.Creator<LinkComment> {
            override fun createFromParcel(source: Parcel): LinkComment = LinkComment(source)
            override fun newArray(size: Int): Array<LinkComment?> = arrayOfNulls(size)
        }
    }
}