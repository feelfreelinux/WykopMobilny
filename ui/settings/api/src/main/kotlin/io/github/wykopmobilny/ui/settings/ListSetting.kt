package io.github.wykopmobilny.ui.settings

class ListSetting<T>(
    val values: Iterable<T>,
    val currentValue: T,
    val isEnabled: Boolean = true,
    val onSelected: (T) -> Unit,
)
