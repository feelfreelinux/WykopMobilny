package io.github.feelfreelinux.wykopmobilny.api.filters

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import java.util.Collections
import javax.inject.Inject

class OWMContentFilter @Inject constructor(
    val blacklistPreferences: BlacklistPreferencesApi,
    val settingsPreferencesApi: SettingsPreferencesApi,
    private val linksPreferencesApi: LinksPreferencesApi
) {

    fun filterEntry(entry: Entry) =
        entry.apply {
            isBlocked =
                    isBlocked ||
                    body.bodyContainsBlockedTags() ||
                    author.nick.isUserBlocked() ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }

    fun filterEntryComment(comment: EntryComment) =
        comment.apply {
            isBlocked =
                    isBlocked ||
                    body.bodyContainsBlockedTags() ||
                    author.nick.isUserBlocked() ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }

    fun filterLinkComment(comment: LinkComment) =
        comment.apply {
            isBlocked =
                    isBlocked ||
                    body?.bodyContainsBlockedTags() ?: false ||
                    author.nick.isUserBlocked() ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }


    fun filterLink(link: Link) =
        link.apply {
            gotSelected = linksPreferencesApi.readLinksIds.contains("link_$id")
            isBlocked =
                    isBlocked ||
                    tags.bodyContainsBlockedTags() ||
                    author?.nick?.isUserBlocked() ?: false ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author?.group == 0)

        }

    private fun String.bodyContainsBlockedTags(): Boolean {
        val tagsRegex = "(^|\\s)(#[a-z\\d-]+)".toRegex()
        return !Collections.disjoint(
            blacklistPreferences.blockedTags,
            tagsRegex.matchEntire(this)?.groupValues?.map { it.removePrefix("#") } ?: emptyList<String>())
    }

    private fun String.isUserBlocked() = blacklistPreferences.blockedUsers.contains(this.removePrefix("@"))
}