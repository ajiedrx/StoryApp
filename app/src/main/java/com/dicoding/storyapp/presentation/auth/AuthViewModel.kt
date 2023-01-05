package com.dicoding.storyapp.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.base.BaseResponse
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.data.auth.model.LoginRequest
import com.dicoding.storyapp.data.auth.model.RegisterRequest
import com.dicoding.storyapp.domain.auth.AuthUseCase
import com.dicoding.storyapp.domain.auth.Login
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthViewModel(private val authInteractor: AuthUseCase) : ViewModel() {

    private var _login: MutableLiveData<BaseResult<Login>> = MutableLiveData<BaseResult<Login>>()
    val login: LiveData<BaseResult<Login>> by lazy { _login }

    private var _register: MutableLiveData<BaseResult<BaseResponse>> = MutableLiveData< BaseResult<BaseResponse>>()
    val register: LiveData<BaseResult<BaseResponse>> by lazy { _register }

    fun login(email: String, password: String){
        viewModelScope.launch {
            authInteractor
                .login(
                    LoginRequest(
                        email, password
                    )
                )
                .onStart {
                    _login.value = BaseResult.Loading
                }
                .catch {
                    _login.value = BaseResult.Error(it.message.orEmpty())
                }
                .collect{
                    _login.value = BaseResult.Success(it)
                }
        }
    }

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            authInteractor
                .register(
                    RegisterRequest(
                        name, email, password
                    )
                )
                .onStart {
                    _register.value = BaseResult.Loading
                }
                .catch {
                    _register.value = BaseResult.Error(it.message.orEmpty())
                }
                .collect{
                    _register.value = BaseResult.Success(it)
                }
        }
    }
}