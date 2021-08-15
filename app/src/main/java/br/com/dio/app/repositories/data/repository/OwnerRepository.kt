package br.com.dio.app.repositories.data.repository

import br.com.dio.app.repositories.data.model.Owner
import kotlinx.coroutines.flow.Flow

interface OwnerRepository {
    fun getUser(user: String): Flow<Owner>
}