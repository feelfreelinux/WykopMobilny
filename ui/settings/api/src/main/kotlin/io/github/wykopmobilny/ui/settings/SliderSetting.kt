package io.github.wykopmobilny.ui.settings

class SliderSetting(
    val values: IntRange,
    val currentValue: Int,
    val isEnabled: Boolean,
    val onChanged: (Int) -> Unit,
)
