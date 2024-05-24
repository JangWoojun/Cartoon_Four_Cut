package com.woojun.cartoon_four_cut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.cartoon_four_cut.adapter.HomePhotoFrameAdapter
import com.woojun.cartoon_four_cut.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.takePhotoButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SelectModeActivity::class.java))
        }

        val adapter = HomePhotoFrameAdapter(mutableListOf())

        binding.photoRecyclerView.adapter = adapter
        binding.photoRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }
}