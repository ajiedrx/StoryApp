package com.dicoding.storyapp.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModel()
    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, RegisterActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAction()
        initObserver()
    }

    private fun initAction(){
        with(binding){
            btnRegister.setOnClickListener {
                authViewModel.register(
                    edRegisterName.text.toString(),
                    edRegisterEmail.text.toString(),
                    edRegisterPassword.text.toString()
                )
            }
        }
    }
    private fun initObserver(){
        authViewModel.register.observe(this) {
            when (it) {
                is BaseResult.Loading -> {
                    showProgressDialog()
                }
                is BaseResult.Success -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Register sukses", Toast.LENGTH_LONG).show()
//                    val intent = Intent(this, DashboardActivity::class.java)
//                    startActivity(intent)
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