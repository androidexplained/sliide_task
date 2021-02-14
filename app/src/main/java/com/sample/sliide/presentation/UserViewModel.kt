package com.sample.sliide.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.sliide.domain.User
import com.sample.sliide.domain.UserUseCase
import com.sample.sliide.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserViewModel(private val usersUseCase: UserUseCase = UserUseCase()) : ViewModel() {

    private val disposables = CompositeDisposable()

    private var removeUserId: Int? = null

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>>
        get() = _users

    private val _errorDialog: MutableLiveData<String> = MutableLiveData()
    val errorDialog: LiveData<String>
        get() = _errorDialog

    private val _addUserDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
    val addUserDialog: LiveData<Unit>
        get() = _addUserDialog

    private val _addUser: SingleLiveEvent<User> = SingleLiveEvent()
    val addUser: LiveData<User>
        get() = _addUser

    private val _removeUserDialog: SingleLiveEvent<Unit> = SingleLiveEvent()
    val removeUserDialog: LiveData<Unit>
        get() = _removeUserDialog

    private val _removeUser: SingleLiveEvent<Int> = SingleLiveEvent()
    val removeUser: LiveData<Int>
        get() = _removeUser

    init {
        loadUsers()
    }

    fun onAddUserClick() {
        _addUserDialog.value = Unit
    }

    fun onAddUserDialogClicked(name: String, email: String) {
        usersUseCase.addUser(name, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::addUserSuccess, this::handleError)
            .addTo(disposables)
    }

    fun onUserViewHolderClick(id: Int) {
        removeUserId = id
        _removeUserDialog.value = Unit
    }

    fun onRemoveWarningConfirmed() {
        val userId = removeUserId ?: return

        usersUseCase.removeUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::removeUserSuccess, this::handleError)
            .addTo(disposables)
    }

    private fun removeUserSuccess(id: Int) {
        _removeUser.value = id
    }

    override fun onCleared() {
        super.onCleared()

        disposables.clear()
    }

    private fun addUserSuccess(user: User) {
        _addUser.value = user
    }

    private fun loadUsers() {
        usersUseCase.users()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleLoadUsersSuccess, this::handleError)
            .addTo(disposables)
    }

    private fun handleLoadUsersSuccess(it: List<User>) {
        _users.value = it
    }

    private fun handleError(error: Throwable) {
        _errorDialog.value = error.message
    }
}

fun Disposable.addTo(disposables: CompositeDisposable) {
    disposables.add(this)
}