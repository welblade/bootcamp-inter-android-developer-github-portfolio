package br.com.dio.app.repositories.presentation.di

import br.com.dio.app.repositories.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModule {
    fun load(){
        loadKoinModules(viewModelModules())
    }

    private fun viewModelModules(): Module {
        return module{
            viewModel { MainViewModel(get(), get())}
        }
    }
}