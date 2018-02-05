package io.github.feelfreelinux.wykopmobilny.models.dataclass

import android.os.Parcel
import android.os.Parcelable


class LinkComment(
        val id: Int,
        val author: Author,
        val date: String,
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
        val embed: Embed?,
        val app: String?,
        var isCollapsed : Boolean,
        var isParentCollapsed : Boolean,
        var childCommentCount : Int,
        var isNsfw : Boolean = false
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
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readInt(),
            1 == source.readInt()
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
        writeInt((if (isCollapsed) 1 else 0))
        writeInt((if (isParentCollapsed) 1 else 0))
        writeInt(childCommentCount)
        writeInt((if (isNsfw) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LinkComment> = object : Parcelable.Creator<LinkComment> {
            override fun createFromParcel(source: Parcel): LinkComment = LinkComment(source)
            override fun newArray(size: Int): Array<LinkComment?> = arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is LinkComment) false
        else (other.id == id)
    }

    override fun hashCode(): Int {
        return id
    }
}