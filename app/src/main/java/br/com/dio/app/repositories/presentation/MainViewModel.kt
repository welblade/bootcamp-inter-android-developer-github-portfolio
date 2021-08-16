package br.com.dio.app.repositories.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dio.app.repositories.data.model.Owner
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.domain.GetUserUseCase
import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val userRepositoriesUseCase: ListUserRepositoriesUseCase, private val ownerUseCase: GetUserUseCase) : ViewModel() {
    private val _owner = MutableLiveData<OwnerState>()
    private val _repos = MutableLiveData<State>()
    val repos: LiveData<State>
        get() { return _repos }
    val owner: LiveData<OwnerState>
        get() = _owner
    fun getRepoList(user: String){
        viewModelScope.launch {
            ownerUseCase(user)
                .onStart { _owner.postValue(OwnerState.Loading) }
                .catch { _owner.postValue(OwnerState.Error(it)) }
                .collect { _owner.postValue(OwnerState.Success(it)) }
            userRepositoriesUseCase(user)
                .onStart { _repos.postValue(State.Loading) }
                .catch { _repos.postValue(State.Error(it)) }
                .collect { _repos.postValue(State.Success(it)) }
        }
    }
    sealed class State {
        object Loading: State()
        data class Success(val list: List<Repo>) : State()
        data class Error(val error: Throwable) : State()
    }
    sealed class OwnerState {
        object Loading: OwnerState()
        data class Success(val owner: Owner) : OwnerState()
        data class Error(val error: Throwable) : OwnerState()
    }
}