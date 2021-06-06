package io.github.wykopmobilny.models.dataclass

class EntryLink(
    var link: Link?,
    var entry: Entry?
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is EntryLink) false
        else (other.hashCode() == hashCode())
    }

    override fun hashCode(): Int {
        return link?.hashCode() ?: entry!!.hashCode()
    }

    companion object {
        const val TYPE_LINK = "LINK"
        const val TYPE_ENTRY = "ENTRY"
    }

    val dataType = if (link == null) TYPE_ENTRY else TYPE_LINK
}
