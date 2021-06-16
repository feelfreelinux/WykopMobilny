package io.github.wykopmobilny.domain.navigation

interface NightModeDetector {

    suspend fun getNightModeState(): NightModeState
}

enum class NightModeState {
    Enabled,
    Disabled,
    Unknown,
}
