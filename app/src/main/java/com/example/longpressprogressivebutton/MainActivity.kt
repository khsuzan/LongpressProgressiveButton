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

        // define id
        progressiveButtonView = findViewById(R.id.progressiveBtn)

        // progress event fire when finished
        progressiveButtonView.onEvent(object : ProgressiveButtonCircular.ProgressiveButton {
            override fun onProgressFinished() {
                Toast.makeText(this@MainActivity, "Completed", Toast.LENGTH_SHORT).show()
            }
        })


        // Button
        val button: Button = findViewById(R.id.button);

        button.setOnClickListener {
            if (progressiveButtonView.isEnabled) {
                // false for disable click
                progressiveButtonView.isEnabled = false;
                button.text = "Click to Enable"
            } else {
                // true for enable click
                progressiveButtonView.isEnabled = true;
                button.text = "Click to Disable"
            }

        }


    }

}

