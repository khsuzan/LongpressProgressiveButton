package com.example.longpressprogressivebutton

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    // variable declaration
    lateinit var progressiveButtonView: ProgressiveButtonView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // define id
        progressiveButtonView = findViewById<ProgressiveButtonView>(R.id.progressiveBtn)

        // progress event fire when finished
        progressiveButtonView.onEvent(object : ProgressiveButtonView.ProgressiveButton {
            override fun onProgressFinished() {
                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_SHORT).show()
            }
        })

    }

}

