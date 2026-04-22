package com.adrc95.domain.usecase

import com.adrc95.domain.model.ThemeMode
import com.adrc95.domain.repository.ConfigurationRepository
import javax.inject.Inject

class ChangeThemeMode @Inject constructor(val configurationRepository: ConfigurationRepository) {
    suspend operator fun invoke(mode: ThemeMode) = configurationRepository.setThemeMode(mode)
}
