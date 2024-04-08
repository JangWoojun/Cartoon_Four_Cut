package com.woojun.cartoon_four_cut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.cartoon_four_cut.databinding.ActivityDownloadBinding

class DownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}