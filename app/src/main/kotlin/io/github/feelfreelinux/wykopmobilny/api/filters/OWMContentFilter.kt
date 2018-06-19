package io.github.feelfreelinux.wykopmobilny.api.filters

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import java.util.*
import javax.inject.Inject

class OWMContentFilter @Inject constructor(val blacklistPreferences: BlacklistPreferencesApi,
                                           val linksPreferencesApi: LinksPreferencesApi,
                                           val settingsPreferencesApi: SettingsPreferencesApi) {
    fun filterEntry(entry : Entry) : Entry {
        return entry.apply {
                isBlocked =
                        isBlocked ||
                        body.bodyContainsBlockedTags() ||
                        author.nick.isUserBlocked() ||
                        (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
            }
    }

    fun filterEntryComment(comment : EntryComment) : EntryComment {
        return comment.apply {
            isBlocked =
                    isBlocked ||
                    body.bodyContainsBlockedTags() ||
                    author.nick.isUserBlocked() ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }
    }

    fun fiterLinkComment(comment : LinkComment) : LinkComment {
        return comment.apply {
            isBlocked =
                    isBlocked ||
                    body?.bodyContainsBlockedTags() ?: false ||
                    author.nick.isUserBlocked() ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author.group == 0)
        }
    }


    fun filterLink(link : Link) : Link {
        return link.apply {
            gotSelected = linksPreferencesApi.readLinksIds.contains("link_$id")
            isBlocked =
                    isBlocked ||
                    tags.bodyContainsBlockedTags() ||
                    author?.nick?.isUserBlocked() ?: false ||
                    (settingsPreferencesApi.hideLowRangeAuthors && author?.group == 0)

        }
    }

    fun String.bodyContainsBlockedTags() : Boolean {
        val tagsRegex = "(^|\\s)(#[a-z\\d-]+)".toRegex()

        return !Collections.disjoint(
                blacklistPreferences.blockedTags,
                tagsRegex.matchEntire(this)?.groupValues?.map { it.removePrefix("#") } ?: emptyList<String>())
    }

    fun String.isUserBlocked() : Boolean {
        return blacklistPreferences.blockedUsers.contains(this.removePrefix("@"))
    }
}