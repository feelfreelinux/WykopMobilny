package io.github.wykopmobilny.domain.settings.prefs

import io.github.wykopmobilny.domain.settings.LinkImagePosition
import io.github.wykopmobilny.domain.settings.UserSettings
import io.github.wykopmobilny.domain.settings.get
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class GetLinksPreferences @Inject constructor(
    private val userPreferences: UserPreferenceApi,
) {

    operator fun invoke() = combine(
        userPreferences.get(UserSettings.useSimpleList),
        userPreferences.get(UserSettings.showLinkThumbnail),
        userPreferences.get(UserSettings.imagePosition),
        userPreferences.get(UserSettings.showAuthor),
        userPreferences.get(UserSettings.cutLinkComments),
    ) { useSimpleList, showLinkThumbnail, imagePosition, showAuthor, cutLinkComments ->
        LinksPreference(
            useSimpleList = useSimpleList ?: false,
            showLinkThumbnail = showLinkThumbnail ?: true,
            imagePosition = imagePosition ?: LinkImagePosition.Left,
            showAuthor = showAuthor ?: false,
            cutLinkComments = cutLinkComments ?: false,
        )
    }
}

internal data class LinksPreference(
    val useSimpleList: Boolean,
    val showLinkThumbnail: Boolean,
    val imagePosition: LinkImagePosition,
    val showAuthor: Boolean,
    val cutLinkComments: Boolean,
)
