package com.dicoding.storyapp.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.const.Const
import com.dicoding.storyapp.data.network.UnauthorizedAccessEvent
import com.dicoding.storyapp.presentation.auth.LoginActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


open class BaseActivity : AppCompatActivity() {
    private var dialog: AlertDialog? = null
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUnauthorizedEvent(event: UnauthorizedAccessEvent) {
        EventBus.getDefault().unregister(this)
        logout()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
        sharedPreferences.edit().putString(Const.LAST_ACTIVITY, javaClass.name).apply()
    }

    fun logout() {
        Intent().apply {
            setClassName(packageName,
                LoginActivity::class.java.name)
            sharedPreferences.edit().clear().apply()
            startActivity(this)
        }
        Toast.makeText(this, "Unauthorized", Toast.LENGTH_LONG).show()
        finishAffinity()
    }

    fun showProgressDialog() {
        val llPadding = 30
        val dialogLayout = LinearLayout(this)
        dialogLayout.orientation = LinearLayout.HORIZONTAL
        dialogLayout.setPadding(llPadding, llPadding, llPadding, llPadding)
        dialogLayout.gravity = Gravity.CENTER
        var dialogLayoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogLayoutParam.gravity = Gravity.CENTER
        dialogLayout.layoutParams = dialogLayoutParam
        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = dialogLayoutParam
        dialogLayoutParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogLayoutParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = getString(R.string.caption_loading)
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = dialogLayoutParam
        dialogLayout.addView(progressBar)
        dialogLayout.addView(tvText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setView(dialogLayout)
        dialog = builder.create()
        dialog?.show()
        val window: Window? = dialog?.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog?.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog?.window?.attributes = layoutParams
        }
    }

    fun hideProgressDialog(){
        dialog?.dismiss()
    }
}