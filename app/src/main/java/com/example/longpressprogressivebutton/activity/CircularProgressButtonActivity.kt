package com.example.longpressprogressivebutton.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.longpressprogressivebutton.R
import com.example.longpressprogressivebutton.databinding.ActivityCircularProgressButtonBinding
import com.example.longpressprogressivebutton.utils.CircularProgressButton


class CircularProgressButtonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCircularProgressButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircularProgressButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBarBackButton()

        binding.btnReset.setOnClickListener {
            binding.circularProgressBar.reset()
        }

        binding.btnEnableDisable.setOnClickListener {
            binding.circularProgressBar.isEnabled = !binding.circularProgressBar.isEnabled
        }

        binding.circularProgressBar.onEvent(object : CircularProgressButton.ProgressiveButton {
            override fun onProgressFinished() {
                Toast.makeText(
                    this@CircularProgressButtonActivity,
                    getString(R.string.completed),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onEnableDisable(isEnable: Boolean) {
                if (isEnable)
                    binding.btnEnableDisable.text = getString(R.string.disable)
                else
                    binding.btnEnableDisable.text = getString(R.string.enable)

            }
        })
    }

    private fun initActionBarBackButton() {
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

