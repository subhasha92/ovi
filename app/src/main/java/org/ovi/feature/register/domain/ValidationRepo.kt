package org.ovi.feature.register.domain

import org.ovi.data.remote.ValidationService
import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.model.ValidationResponse

class ValidationRepo(private val validationService: ValidationService):IValidationRepo {
    override suspend fun validEmail(request: RegisterRequest): ValidationResponse {
        return validationService.validEmail(request)
    }

    override suspend fun validPhone(request: RegisterRequest): ValidationResponse {
        return validationService.validPhone(request)
    }
}