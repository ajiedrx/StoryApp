package com.dicoding.storyapp.presentation.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.const.Const
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.domain.auth.Login
import com.dicoding.storyapp.presentation.story.list.StoryListActivity
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    companion object{
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            starter.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityLoginBinding

    private val authViewModel: AuthViewModel by viewModel()

    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUserLogin()

        initAction()
        initObserver()
    }
    private fun checkUserLogin() {
        if(sharedPreferences.contains(Const.USER_LOGIN_INFO)) {
            if(!Gson().fromJson(sharedPreferences.getString(Const.USER_LOGIN_INFO, ""), Login::class.java).token.isNullOrEmpty()) {
                StoryListActivity.start(this@LoginActivity)
                this.finish()
            } else {
                val activityClass: Class<*>? = try {
                    Class.forName(
                        sharedPreferences.getString(Const.LAST_ACTIVITY, LoginActivity::class.java.name).toString())
                } catch (ex: ClassNotFoundException) {
                    ex.printStackTrace()
                    LoginActivity::class.java
                }

                if (activityClass != this.javaClass) {
                    startActivity(Intent(this, activityClass))
                }
            }
        }
    }
    private fun initAction(){
        with(binding){
            btnLogin.setOnClickListener {
                authViewModel.login(edLoginEmail.text.toString(), edLoginPassword.text.toString())
            }
            btnRegister.setOnClickListener {
                RegisterActivity.start(this@LoginActivity)
            }
        }
    }
    private fun initObserver(){
        authViewModel.login.observe(this) {
            when (it) {
                is BaseResult.Loading -> {
                    showProgressDialog()
                }
                is BaseResult.Success -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Login sukses: " + it.data.name, Toast.LENGTH_LONG).show()
                    StoryListActivity.start(this@LoginActivity)
                    finish()
                }
                is BaseResult.Error -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                }
                else -> {
                    hideProgressDialog()
                }
            }
        }
    }
}