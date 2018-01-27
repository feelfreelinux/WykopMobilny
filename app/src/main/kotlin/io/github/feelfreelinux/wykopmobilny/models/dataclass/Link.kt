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
        var gotSelected : Boolean
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            ArrayList<LinkComment>().apply { source.readList(this, LinkComment::class.java.classLoader) },
            source.readInt(),
            source.readInt(),
            source.readParcelable<Author>(Author::class.java.classLoader),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readString(),
            1 == source.readInt()
            )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeString(description)
        writeString(tags)
        writeString(sourceUrl)
        writeInt(voteCount)
        writeInt(buryCount)
        writeList(comments)
        writeInt(commentsCount)
        writeInt(relatedCount)
        writeParcelable(author, 0)
        writeString(date)
        writeString(preview)
        writeInt((if (plus18) 1 else 0))
        writeInt((if (canVote) 1 else 0))
        writeInt((if (isHot) 1 else 0))
        writeString(status)
        writeString(userVote)
        writeInt((if (userFavorite) 1 else 0))
        writeString(app)
        writeInt((if (gotSelected) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Link> = object : Parcelable.Creator<Link> {
            override fun createFromParcel(source: Parcel): Link = Link(source)
            override fun newArray(size: Int): Array<Link?> = arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is Link) false
        else (other.id == id)
    }

    override fun hashCode(): Int {
        return id
    }
}