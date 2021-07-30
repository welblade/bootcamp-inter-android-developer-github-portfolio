package br.com.dio.app.repositories.data.di

import android.util.Log
import br.com.dio.app.repositories.data.repository.RepoRepository
import br.com.dio.app.repositories.data.repository.RepoRepositoryImpl
import br.com.dio.app.repositories.data.service.GitHubService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {
    private const val OK_HTTP = "OkHttp"
    fun load(){
        loadKoinModules(networkModules() + repositoryModules())
    }
    private fun networkModules(): Module {
        return module {
            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.e(OK_HTTP, it)
                }.also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                }
                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }
            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }
            single {
                createService<GitHubService>(get(), get())
            }
        }
    }
    private fun repositoryModules(): Module{
        return module {
            single<RepoRepository> {
                RepoRepositoryImpl(get())
            }
        }
    }
    private inline fun <reified T> createService(client: OkHttpClient, factory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(T::class.java)
    }
}