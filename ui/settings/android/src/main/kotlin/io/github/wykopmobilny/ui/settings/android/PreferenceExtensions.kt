package io.github.wykopmobilny.ui.settings.android

import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import io.github.wykopmobilny.ui.settings.ListSetting
import io.github.wykopmobilny.ui.settings.Setting
import io.github.wykopmobilny.ui.settings.SliderSetting

internal fun PreferenceFragmentCompat.bindPreference(key: String, onClick: (() -> Unit)?) {
    val pref = findPreference<Preference>(key) ?: return
    pref.isVisible = onClick != null
    pref.setOnPreferenceClickListener { onClick?.invoke(); true }
}

internal fun PreferenceFragmentCompat.bindCheckbox(key: String, setting: Setting) {
    val pref = findPreference<CheckBoxPreference>(key) ?: return
    pref.isChecked = setting.currentValue
    pref.isEnabled = setting.isEnabled
    pref.setOnPreferenceClickListener { setting.onClicked(); true }
}

internal fun <T> PreferenceFragmentCompat.bindList(
    key: String,
    setting: ListSetting<T>,
    mapping: Map<T, String>,
) {
    val pref = findPreference<ListPreference>(key) ?: return
    pref.entries = setting.values.mapNotNull { mapping[it] }.toTypedArray()
    pref.entryValues = pref.entries
    pref.value = mapping[setting.currentValue]
    pref.summary = mapping[setting.currentValue]
    pref.isEnabled = setting.isEnabled
    pref.setOnPreferenceChangeListener { _, newValue ->
        setting.onSelected(mapping.entries.first { (_, value) -> newValue.toString() == value }.key)
        true
    }
}

internal fun PreferenceFragmentCompat.bindSlider(key: String, setting: SliderSetting) {
    val pref = findPreference<SeekBarPreference>(key) ?: return
    pref.min = setting.values.first
    pref.max = setting.values.last
    pref.value = setting.currentValue
    pref.isEnabled = setting.isEnabled
    pref.setOnPreferenceChangeListener { _, newValue -> setting.onChanged(newValue as Int); true }
}
