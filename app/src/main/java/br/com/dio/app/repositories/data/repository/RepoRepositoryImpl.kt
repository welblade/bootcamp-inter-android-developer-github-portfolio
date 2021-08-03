package br.com.dio.app.repositories.data.repository

import br.com.dio.app.repositories.core.RemoteException
import br.com.dio.app.repositories.data.service.GitHubService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class RepoRepositoryImpl(private val service: GitHubService) : RepoRepository {
    override fun listRepository(user: String) = flow {
        try {
            val repoList = service.listRepositories(user)
            emit(repoList)
        }catch (err: HttpException){
            throw(RemoteException(err.message ?: "Não é possível realizar a busca no momento." ))
        }

    }

}