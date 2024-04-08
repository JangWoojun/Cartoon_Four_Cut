package com.woojun.cartoon_four_cut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.cartoon_four_cut.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}