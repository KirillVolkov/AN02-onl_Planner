package io.techmeskills.an02onl_plannerapp

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import io.techmeskills.an02onl_plannerapp.cloud.ApiInterface
import io.techmeskills.an02onl_plannerapp.database.DatabaseConstructor
import io.techmeskills.an02onl_plannerapp.database.PlannerDatabase
import io.techmeskills.an02onl_plannerapp.datastore.AppSettings
import io.techmeskills.an02onl_plannerapp.repository.CloudRepository
import io.techmeskills.an02onl_plannerapp.repository.NotesRepository
import io.techmeskills.an02onl_plannerapp.repository.NotificationRepository
import io.techmeskills.an02onl_plannerapp.repository.UsersRepository
import io.techmeskills.an02onl_plannerapp.screen.login.LoginViewModel
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.screen.note_details.NoteDetailsViewModel
import io.techmeskills.an02onl_plannerapp.screen.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlannerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlannerApp)
            modules(listOf(viewModels, storageModule, repositoryModule, cloudModule, systemModule))
        }
    }

    private val viewModels = module {
        viewModel { MainViewModel(get()) }
        viewModel { NoteDetailsViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { SettingsViewModel(get(), get()) }
    }

    private val storageModule = module {
        single { DatabaseConstructor.create(get()) }  //создаем синглтон базы данных
        factory { get<PlannerDatabase>().notesDao() } //предоставляем доступ для конкретной Dao (в нашем случае NotesDao)
        factory { get<PlannerDatabase>().usersDao() }
        single { AppSettings(get()) } //DataStore
    }

    private val repositoryModule = module {  //создаем репозитории
        factory { UsersRepository(get(), get(), get()) }
        factory { NotesRepository(get(), get(), get(),get()) }
        factory { CloudRepository(get(), get(), get()) }
        factory { NotificationRepository(get(), get()) }
    }

    private val cloudModule = module {
        factory { ApiInterface.get() }
    }

    private val systemModule = module {
        factory { get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    }
}
