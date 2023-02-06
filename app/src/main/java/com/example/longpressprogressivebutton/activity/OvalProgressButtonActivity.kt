package com.example.longpressprogressivebutton.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.longpressprogressivebutton.R
import com.example.longpressprogressivebutton.databinding.ActivityOvalProgressButtonBinding
import com.example.longpressprogressivebutton.utils.OvalProgressButton

class OvalProgressButtonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOvalProgressButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOvalProgressButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBarBackButton()

        binding.btnReset.setOnClickListener {
            binding.ovalProgressBar.reset()
        }

        binding.btnEnableDisable.setOnClickListener {
            binding.ovalProgressBar.isEnabled = !binding.ovalProgressBar.isEnabled
        }

        binding.ovalProgressBar.onEvent(object : OvalProgressButton.ProgressiveButton {
            override fun onProgressFinished() {
                Toast.makeText(
                    this@OvalProgressButtonActivity,
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