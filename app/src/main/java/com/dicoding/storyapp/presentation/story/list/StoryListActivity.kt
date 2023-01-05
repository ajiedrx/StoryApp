package com.dicoding.storyapp.presentation.story.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.databinding.ActivityStoryListBinding
import com.dicoding.storyapp.presentation.auth.LoginActivity
import com.dicoding.storyapp.presentation.story.StoryViewModel
import com.dicoding.storyapp.presentation.story.add.AddStoryActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryListActivity : BaseActivity() {
    companion object{
        fun start(context: Context) {
            val starter = Intent(context, StoryListActivity::class.java)
            starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(starter)
        }
    }

    private var _binding: ActivityStoryListBinding? = null
    private val binding get() = _binding!!

    private val storyViewModel: StoryViewModel by viewModel()

    private val sharedPreferencesEditor: SharedPreferences.Editor by inject()

    private val storyAdapter: StoryListAdapter by lazy {
        StoryListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initAction()
        initObserver()
        storyViewModel.getAllStories()
    }

    private fun initAction(){
        with(binding){
            btnLogout.setOnClickListener {
                sharedPreferencesEditor.clear().apply()
                LoginActivity.start(this@StoryListActivity)
                super.finishAffinity()
            }
            btnAddStory.setOnClickListener {
                AddStoryActivity.startActivityForResult(this@StoryListActivity, launcherAddStory)
            }
        }
    }

    private fun initUI(){
        with(binding){
            rvStoryList.apply {
                adapter = storyAdapter
                layoutManager = LinearLayoutManager(this@StoryListActivity)
            }
        }
    }
    private fun initObserver(){
        storyViewModel.getAllStories.observe(this) {
            when (it) {
                is BaseResult.Loading -> {
                    showProgressDialog()
                }
                is BaseResult.Success -> {
                    hideProgressDialog()
                    storyAdapter.setData(it.data)
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

    private val launcherAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddStoryActivity.ADD_STORY_RESULT_OK) {
            storyViewModel.getAllStories()
        }
    }
}