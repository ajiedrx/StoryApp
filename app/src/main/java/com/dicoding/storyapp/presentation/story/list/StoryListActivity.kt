package com.dicoding.storyapp.presentation.story.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.databinding.ActivityStoryListBinding
import com.dicoding.storyapp.presentation.adapter.LoadingStateAdapter
import com.dicoding.storyapp.presentation.adapter.StoryListPagingAdapter
import com.dicoding.storyapp.presentation.auth.LoginActivity
import com.dicoding.storyapp.presentation.maps.MapsActivity
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

    private val storyPagingAdapter: StoryListPagingAdapter by lazy {
        StoryListPagingAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initAction()
        initObserver()
//
//        storyViewModel.getAllStoriesPaging()
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
            btnMaps.setOnClickListener {
                MapsActivity.start(this@StoryListActivity)
            }
        }
    }

    private fun initUI(){
        with(binding){
            rvStoryList.apply {
                adapter = storyPagingAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        storyPagingAdapter.retry()
                    }
                )
                layoutManager = LinearLayoutManager(this@StoryListActivity)
            }
        }
    }
    private fun initObserver(){
        storyViewModel.getAllStoriesPaging().observe(this) {
            storyPagingAdapter.submitData(lifecycle, it)
        }
    }

    private val launcherAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddStoryActivity.ADD_STORY_RESULT_OK) {
            storyViewModel.getAllStoriesPaging()
        }
    }
}