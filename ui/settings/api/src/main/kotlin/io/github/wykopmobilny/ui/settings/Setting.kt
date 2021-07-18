package io.github.wykopmobilny.ui.settings

class Setting(
    val currentValue: Boolean,
    val isEnabled: Boolean = true,
    val onClicked: () -> Unit,
)
