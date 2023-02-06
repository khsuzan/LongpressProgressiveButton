package com.example.longpressprogressivebutton.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.longpressprogressivebutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCircularProgressBar.setOnClickListener {
            startActivity(Intent(this@MainActivity, CircularProgressButtonActivity::class.java))
        }

        binding.btnOvalProgressBar.setOnClickListener {
            startActivity(Intent(this@MainActivity, OvalProgressButtonActivity::class.java))
        }
    }
}