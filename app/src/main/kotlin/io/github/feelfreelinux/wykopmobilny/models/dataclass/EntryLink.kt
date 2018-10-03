package io.github.feelfreelinux.wykopmobilny.models.dataclass

class EntryLink(
    var link: Link?,
    var entry: Entry?
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is EntryLink) false
        else (other.link != null && link != null && other.link!!.id == link!!.id && other.entry != null && entry != null && other.entry!!.id == entry!!.id)
    }

    override fun hashCode(): Int {
        return link?.hashCode() ?: entry!!.hashCode()
    }

    companion object {
        const val TYPE_LINK = "LINK"
        const val TYPE_ENTRY = "ENTRY"
    }

    val DATA_TYPE = if (link == null) TYPE_ENTRY else TYPE_LINK
}