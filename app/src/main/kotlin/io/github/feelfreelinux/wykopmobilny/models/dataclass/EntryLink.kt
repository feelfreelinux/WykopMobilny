package io.github.feelfreelinux.wykopmobilny.models.dataclass

class EntryLink (
        val link: Link?,
        val entry : Entry?
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is EntryLink) false
        else (other.link != null && link != null && other.link.id == link.id && other.entry != null && entry != null && other.entry.id == entry.id)
    }

    override fun hashCode(): Int {
        return link?.hashCode() ?: entry!!.hashCode()
    }
}