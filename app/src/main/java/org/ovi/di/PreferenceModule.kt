package org.ovi.di

import org.ovi.data.pref.OVIPreferences
import org.koin.dsl.module

val preferenceModule = module {
    single { providePref() }
}

fun providePref() = OVIPreferences()