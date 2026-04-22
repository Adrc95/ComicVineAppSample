package com.adrc95.domain.usecase

import com.adrc95.domain.repository.ConfigurationRepository
import javax.inject.Inject

class GetThemeMode @Inject constructor(private val configurationRepository: ConfigurationRepository) {
    operator fun invoke() = configurationRepository.getThemeMode()
}
