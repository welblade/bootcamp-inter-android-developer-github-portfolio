package br.com.dio.app.repositories.domain.di

import br.com.dio.app.repositories.domain.GetUserUseCase
import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {
    fun load(){
        loadKoinModules(useCaseModules())
    }

    private fun useCaseModules(): Module {
        return module{
            factory {
                ListUserRepositoriesUseCase(get())
            }
            factory {
                GetUserUseCase(get())
            }
        }
    }
}