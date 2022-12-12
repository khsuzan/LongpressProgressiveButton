package com.example.longpressprogressivebutton

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    // variable declaration
    lateinit var progressiveButtonView: ProgressiveButtonCircular

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Button
        val button: Button = findViewById(R.id.button)
        // Reset Button
        val restbutton: Button = findViewById(R.id.reset)

        // define id
        progressiveButtonView = findViewById(R.id.progressiveBtn)

        // progress event fire when finished
        progressiveButtonView.onEvent(object : ProgressiveButtonCircular.ProgressiveButton {
            override fun onProgressFinished() {
                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_SHORT).show()
            }

            override fun onEnableDisable(isEnable: Boolean) {
                if (isEnable)
                    button.text = "Tap to Disable"
                else
                    button.text = "Tap to Enable"

            }
        })



        button.setOnClickListener {
            progressiveButtonView.isEnabled = !progressiveButtonView.isEnabled
        }

        restbutton.setOnClickListener {
            progressiveButtonView.reset()

        }


    }

}

