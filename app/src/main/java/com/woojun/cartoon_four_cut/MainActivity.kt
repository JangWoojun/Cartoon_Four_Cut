package com.woojun.cartoon_four_cut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.cartoon_four_cut.adapter.HomePhotoFrameAdapter
import com.woojun.cartoon_four_cut.database.AppDatabase
import com.woojun.cartoon_four_cut.databinding.ActivityMainBinding
import com.woojun.cartoon_four_cut.util.OnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var backPressedTime: Long = 0
    private val backPressInterval: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.takePhotoButton.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                startActivity(Intent(this@MainActivity, SelectModeActivity::class.java))
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            val homePhotoFrameDao = AppDatabase.getDatabase(this@MainActivity)?.homePhotoFrameItemDao()
            val adapter = HomePhotoFrameAdapter(homePhotoFrameDao!!.getHomePhotoFrameList())
            HomePhotoFrameAdapter(mutableListOf())
            withContext(Dispatchers.Main) {
                binding.photoRecyclerView.adapter = adapter
                binding.photoRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            if (it.getBooleanExtra("UPDATE", false)) {
                CoroutineScope(Dispatchers.IO).launch {
                    val homePhotoFrameDao = AppDatabase.getDatabase(this@MainActivity)?.homePhotoFrameItemDao()
                    val adapter = HomePhotoFrameAdapter(homePhotoFrameDao!!.getHomePhotoFrameList())
                    HomePhotoFrameAdapter(mutableListOf())
                    withContext(Dispatchers.Main) {
                        binding.photoRecyclerView.adapter = adapter
                        binding.photoRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + backPressInterval > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(this, "한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}