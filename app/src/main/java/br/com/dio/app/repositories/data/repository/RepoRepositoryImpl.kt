package br.com.dio.app.repositories.data.repository

import br.com.dio.app.repositories.data.service.GitHubService
import kotlinx.coroutines.flow.flow

class RepoRepositoryImpl(private val service: GitHubService) : RepoRepository {
    override fun listRepository(user: String) = flow {
        val repoList = service.listRepositories(user)
        emit(repoList)
    }

}