package io.github.wykopmobilny.domain.settings.prefs

import io.github.wykopmobilny.domain.settings.UserSettings
import io.github.wykopmobilny.domain.settings.get
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class GetImagesPreferences @Inject constructor(
    private val userPreferences: UserPreferenceApi,
) {

    operator fun invoke() = combine(
        userPreferences.get(UserSettings.showMinifiedImages),
        userPreferences.get(UserSettings.cutImages),
        userPreferences.get(UserSettings.cutImagesProportion),
    ) { showMinifiedImages, cutImages, cutImagesProportion ->
        ImagePreferences(
            showMinifiedImages = showMinifiedImages ?: false,
            cutImages = cutImages ?: true,
            cutImagesProportion = cutImagesProportion?.toInt() ?: DEFAULT_IMAGE_CUT_PROPORTION,
        )
    }

    companion object {
        const val DEFAULT_IMAGE_CUT_PROPORTION = 60
        const val CUT_IMAGES_RANGE_FROM = 20
        const val CUT_IMAGES_RANGE_TO = 150
    }
}

data class ImagePreferences(
    val showMinifiedImages: Boolean,
    val cutImages: Boolean,
    val cutImagesProportion: Int,
)
