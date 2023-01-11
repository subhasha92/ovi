package org.ovi.feature.register.domain

import org.ovi.feature.register.model.RegisterRequest
import org.ovi.feature.register.model.ValidationResponse

interface IValidationRepo {

    suspend fun validEmail(request: RegisterRequest): ValidationResponse
    suspend fun validPhone(request: RegisterRequest): ValidationResponse

}