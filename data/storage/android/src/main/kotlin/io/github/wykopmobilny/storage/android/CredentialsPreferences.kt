package io.github.wykopmobilny.utils.api

import android.content.Context
import io.github.wykopmobilny.storage.android.Preferences
import io.github.wykopmobilny.storage.api.CredentialsPreferencesApi
import javax.inject.Inject

internal class CredentialsPreferences @Inject constructor(
    context: Context
) : Preferences(context), CredentialsPreferencesApi {

    override var login by stringPref(defaultValue = "")
    override var userKey by stringPref(defaultValue = "")
    override var userToken by stringPref(defaultValue = "")
    override var avatarUrl by stringPref(defaultValue = "")
    override var backgroundUrl by stringPref(defaultValue = "")
    override var timeStamp by stringPref(defaultValue = "")
}
