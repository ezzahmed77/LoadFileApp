package com.example.android.finalloadapp


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get File Name And status content texts
        val fileNameContent: TextView = findViewById(R.id.fileNameTextContent)
        val statusContent : TextView = findViewById(R.id.statusTextContent)

        // Getting fileName and Status from bundle
        val statusDownload = intent.getStringExtra("Status")
        val fileName = intent.getStringExtra("FileName")

        if(statusDownload == "Fail"){
            statusContent.setTextColor(Color.RED)
        }

        fileNameContent.text = fileName
        statusContent.text = statusDownload

        // Setting onclickListener for okButton
        val okButton : Button = findViewById(R.id.okButton)
        okButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}