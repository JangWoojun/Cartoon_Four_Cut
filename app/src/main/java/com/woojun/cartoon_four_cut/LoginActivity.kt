package com.woojun.cartoon_four_cut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.cartoon_four_cut.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}