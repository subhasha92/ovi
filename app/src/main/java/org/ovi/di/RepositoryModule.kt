package org.ovi.di

import org.koin.dsl.module
import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.*
import org.ovi.feature.auth.domain.AuthRepo
import org.ovi.feature.auth.domain.IAuthRepo
import org.ovi.feature.events.domain.EventsRepo
import org.ovi.feature.events.domain.IEventRepo
import org.ovi.feature.notification.domain.INotificationRepo
import org.ovi.feature.notification.domain.NotificationRepo
import org.ovi.feature.profile.domain.*
import org.ovi.feature.register.domain.*
import org.ovi.feature.survey.domain.ISurveyRepo
import org.ovi.feature.survey.domain.SurveyRepo

val repositoryModule = module {
    single { provideAuthRepo(get(), get()) }
    single { provideRegisterRepo(get(), get()) }
    single { provideProfileRepo(get(), get()) }
    single { provideEventsRepo(get(), get()) }
    single { provideMasterRepo(get(), get()) }
    single { provideNotificationRepo(get(), get()) }
    single { provideSurveyRepo(get(), get()) }
    single { provideFileUploadRepo(get()) }
    single { provideNewFileUploadRepo(get()) }
    single { provideValidationRepo(get()) }

}

private fun provideAuthRepo(authService: AuthService, oviPreferences: OVIPreferences): IAuthRepo {
    return AuthRepo(authService, oviPreferences)
}

private fun provideRegisterRepo(
    registerService: RegisterService,
    oviPreferences: OVIPreferences
): IRegisterRepo {
    return RegisterRepo(registerService, oviPreferences)
}

private fun provideProfileRepo(
    profileServices: ProfileServices,
    oviPreferences: OVIPreferences
): IProfileRepo {
    return ProfileRepo(profileServices, oviPreferences)
}

private fun provideEventsRepo(
    eventService: EventService,
    oviPreferences: OVIPreferences
): IEventRepo {
    return EventsRepo(eventService, oviPreferences)
}

private fun provideMasterRepo(
    profileServices: ProfileServices,
    oviPreferences: OVIPreferences
): IMasterRepo {
    return MasterRepo(profileServices, oviPreferences)
}

private fun provideFileUploadRepo(fileFileUploadService: FileUploadService): IFileUploadRepo {
    return FileUploadRepo(fileFileUploadService)
}

private fun provideNotificationRepo(
    notificationService: NotificationService,
    oviPreferences: OVIPreferences
): INotificationRepo {
    return NotificationRepo(notificationService, oviPreferences)
}

private fun provideNewFileUploadRepo(newFileUploadService: NewFileUploadService): INewFileUploadRepo {
    return NewFileUploadRepo(newFileUploadService)
}

private fun provideValidationRepo(validationService: ValidationService): IValidationRepo {
    return ValidationRepo(validationService)
}

private fun provideSurveyRepo(surveyService: SurveyService,oviPreferences: OVIPreferences):ISurveyRepo{
    return SurveyRepo(surveyService,oviPreferences)
}