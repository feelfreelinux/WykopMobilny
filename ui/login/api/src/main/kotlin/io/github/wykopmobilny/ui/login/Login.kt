package io.github.wykopmobilny.ui.login

import io.github.wykopmobilny.ui.base.Query

interface Login : Query<LoginUi>

data class LoginUi(
    val urlToLoad: String,
    val isLoading: Boolean,
    val visibleError: InfoMessageUi?,
    val parseUrlAction: (String) -> Unit,
)

data class InfoMessageUi(
    val title: String,
    val message: String,
    val confirmAction: () -> Unit,
    val dismissAction: () -> Unit,
)
