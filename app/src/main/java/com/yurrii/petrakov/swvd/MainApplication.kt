package com.yurrii.petrakov.swvd

import android.app.Application
import com.yurrii.petrakov.swvd.data.analytics.AnalyticsRepository
import com.yurrii.petrakov.swvd.data.analytics.AnalyticsTrackerImpl
import com.yurrii.petrakov.swvd.data.local.database.createAnalyticsDao
import com.yurrii.petrakov.swvd.data.local.database.createDatabase
import com.yurrii.petrakov.swvd.data.local.prefs.ContentGatePreferences
import com.yurrii.petrakov.swvd.data.repository.ContentGateRepositoryImpl
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.domain.repository.ContentGateRepository
import com.yurrii.petrakov.swvd.domain.util.UrlHandler
import com.yurrii.petrakov.swvd.presentation.control_panel.ControlPanelViewModel
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewModel
import com.yurrii.petrakov.swvd.ui.CommonViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    viewModelOf(::ControlPanelViewModel)
    viewModelOf(::WebViewModel)
    singleOf(::createDatabase)
    singleOf(::createAnalyticsDao)
    singleOf(::AnalyticsRepository)
    single<AnalyticsTracker>{ AnalyticsTrackerImpl(get()) }
    viewModelOf(::CommonViewModel)
    singleOf(::ContentGatePreferences)
    singleOf(::UrlHandler)
    single<ContentGateRepository>{ContentGateRepositoryImpl(get())}

}

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

}