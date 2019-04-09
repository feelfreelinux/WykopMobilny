package io.github.feelfreelinux.wykopmobilny.api.filters

import io.github.feelfreelinux.wykopmobilny.api.patrons.PatronsApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons.PatronBadge
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import java.util.Collections
import javax.inject.Inject

class OWMContentFilter @Inject constructor(
    val blacklistPreferences: BlacklistPreferencesApi,
    val settingsPreferencesApi: SettingsPreferencesApi,
    private val linksPreferencesApi: LinksPreferencesApi,
    val patronsApi: PatronsApi
) {

    fun getBadgeFor(author: Author): PatronBadge? {
        return try {
            patronsApi.patrons.firstOrNull { it.username == author.nick }?.badge
        } catch (e: Throwable) {
            null
        }
    }

    fun filterEntry(entry: Entry) =
        entry.apply {
            author.badge = getBadgeFor(author)
            isBlocked =
                    isBlocked ||
                    body.bodyContainsBlockedTags() ||
                    author.nick.isUserBlocked() ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0) ||
                    (settingsPreferencesApi.hideContentWithoutTags && !body.bodyContainsTags())
        }

    fun filterEntryComment(comment: EntryComment) =
        comment.apply {
            author.badge = getBadgeFor(author)
            isBlocked =
                    isBlocked ||
                    body.bodyContainsBlockedTags() ||
                    author.nick.isUserBlocked() ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }

    fun filterLinkComment(comment: LinkComment) =
        comment.apply {
            author.badge = getBadgeFor(author)
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

    private val tagsRegex = "(^|\\s)(#[a-z\\d-]+)".toRegex()

    private fun String.bodyContainsTags() = tagsRegex.containsMatchIn(this.removeHtml())

    private fun String.bodyContainsBlockedTags(): Boolean {
        return !Collections.disjoint(
            blacklistPreferences.blockedTags,
            tagsRegex.matchEntire(this)?.groupValues?.map { it.removePrefix("#") } ?: emptyList<String>())
    }

    private fun String.isUserBlocked() = blacklistPreferences.blockedUsers.contains(this.removePrefix("@"))
}