package org.ovi.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module
import org.ovi.feature.auth.viewmodel.AuthViewModel
import org.ovi.feature.events.viewmodel.EventsViewModel
import org.ovi.feature.home.viewmodel.HomeViewModel
import org.ovi.feature.notification.viewmodel.NotificationViewModel
import org.ovi.feature.profile.viewmodel.FileUploadViewModel
import org.ovi.feature.profile.viewmodel.NewFIleUploadViewModel
import org.ovi.feature.profile.viewmodel.ProfileViewModel
import org.ovi.feature.register.viewmodel.MasterViewModel
import org.ovi.feature.register.viewmodel.RegisterViewModel
import org.ovi.feature.register.viewmodel.ValidationViewModel
import org.ovi.feature.survey.viewmodel.SurveyViewModel

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { (type: Int) -> ProfileViewModel(get(), type) }
    viewModel { (type: Int) -> EventsViewModel(get(), type) }
    viewModel { MasterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { FileUploadViewModel(get()) }
    viewModel { NewFIleUploadViewModel(get()) }
    viewModel { ValidationViewModel(get()) }
    viewModel { SurveyViewModel(get()) }

}