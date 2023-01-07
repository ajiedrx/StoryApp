package com.dicoding.storyapp.presentation.maps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dicoding.storyapp.R
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.domain.story.Story
import com.dicoding.storyapp.presentation.story.StoryViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val storyViewModel: StoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

         /* = java.util.HashMap<kotlin.Double, kotlin.Double> */
    }

    private fun initObserver(){
        storyViewModel.getAllStories.observe(this) {
            when (it) {
                is BaseResult.Loading -> {
                    showProgressDialog()
                }
                is BaseResult.Success -> {
                    hideProgressDialog()
                    bindLocationData(it.data)
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        storyViewModel.getAllStories(true)
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    }

    private fun bindLocationData(data: List<Story>){
        data.forEach {
            mMap.addMarker(MarkerOptions().position(LatLng(it.lat, it.lon)))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lon)))
        }
    }


    companion object{
        fun start(context: Context) {
            val starter = Intent(context, MapsActivity::class.java)
            context.startActivity(starter)
        }
    }
}