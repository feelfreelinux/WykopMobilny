package io.github.wykopmobilny.api.filters

import io.github.wykopmobilny.api.patrons.PatronsApi
import io.github.wykopmobilny.api.patrons.getBadgeFor
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.github.wykopmobilny.storage.api.LinksPreferencesApi
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.utils.textview.removeHtml
import java.util.Collections
import javax.inject.Inject

class OWMContentFilter @Inject constructor(
    private val blacklistPreferences: BlacklistPreferencesApi,
    private val settingsPreferencesApi: SettingsPreferencesApi,
    private val linksPreferencesApi: LinksPreferencesApi,
    private val patronsApi: PatronsApi
) {

    fun filterEntry(entry: Entry) =
        entry.apply {
            author.badge = patronsApi.getBadgeFor(author)
            isBlocked =
                isBlocked ||
                body.bodyContainsBlockedTags() ||
                author.nick.isUserBlocked() ||
                (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0) ||
                (settingsPreferencesApi.hideContentWithoutTags && !body.bodyContainsTags())
        }

    fun filterEntryComment(comment: EntryComment) =
        comment.apply {
            author.badge = patronsApi.getBadgeFor(author)
            isBlocked =
                isBlocked ||
                body.bodyContainsBlockedTags() ||
                author.nick.isUserBlocked() ||
                (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }

    fun filterLinkComment(comment: LinkComment) =
        comment.apply {
            author.badge = patronsApi.getBadgeFor(author)
            isBlocked =
                isBlocked ||
                body?.bodyContainsBlockedTags() ?: false ||
                author.nick.isUserBlocked() ||
                (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }

    fun filterLink(link: Link) =
        link.apply {
            gotSelected = linksPreferencesApi.readLinksIds.orEmpty().contains("link_$id")
            isBlocked =
                isBlocked ||
                tags.bodyContainsBlockedTags() ||
                author?.nick?.isUserBlocked() ?: false ||
                (settingsPreferencesApi.hideLowRangeAuthors && author?.group == 0)
        }

    private val tagsRegex = "(^|\\s)(#[a-z\\d-]+)".toRegex()

    private fun String.bodyContainsTags() = tagsRegex.containsMatchIn(this.removeHtml())

    private fun String.bodyContainsBlockedTags(): Boolean {
        return !Collections.disjoint(
            blacklistPreferences.blockedTags.orEmpty(),
            tagsRegex.matchEntire(this)?.groupValues?.map { it.removePrefix("#") } ?: emptyList<String>()
        )
    }

    private fun String.isUserBlocked() = blacklistPreferences.blockedUsers.orEmpty().contains(this.removePrefix("@"))
}