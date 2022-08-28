package com.example.android.finalloadapp

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


// All URLs
private const val GLIDE_URL = "https://github.com/bumptech/glide"
private const val APP_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
private const val RETROFIT_URL = "https://github.com/square/retrofit"


// For Notification
private const val CHANNEL_ID = "downloadStatus"
private const val CHANNEL_NAME = "downloadFile"
private const val NOTIFICATION_ID = 0

// For Success And Failed Download
private const val SUCCESS_DOWNLOAD = "File Downloaded Successfully"
private const val FAILED_DOWNLOAD = "Failed To Download File"



class MainActivity : AppCompatActivity() {

    // For Views
    private lateinit var downloadRadioGroup : RadioGroup

    private lateinit var glideRadioButton: RadioButton
    private lateinit var appRadioButton: RadioButton
    private lateinit var retrofitRadioButton: RadioButton

    // For ButtonView
    private lateinit var buttonView: ButtonView

    // For CircleView
    private lateinit var circleView : CircleView

    // Getting Texts For Radio Buttons
    private lateinit var glideRadioButtonText : String
    private lateinit var appRadioButtonText : String
    private lateinit var retrofitRadioButtonText : String

    // For the downloading Id
    private var downloadId : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing views
        initViews()

        // Creating notification Channel
        createNotificationChannel()

        // Setting onclickListener for the ButtonView
        buttonView.setOnClickListener {
            checkAndStartDownloading()
        }


    }



    // Method To Initialize The Views
    private fun initViews() {
        downloadRadioGroup = findViewById(R.id.downloadRadioGroup)

        glideRadioButton = findViewById(R.id.glideDownlaodRadioButton)
        appRadioButton = findViewById(R.id.appDownloadRadioButton)
        retrofitRadioButton = findViewById(R.id.retrofitDownloadRadioButton)

        // Getting texts of radio buttons
        glideRadioButtonText = glideRadioButton.text.toString()
        appRadioButtonText = appRadioButton.text.toString()
        retrofitRadioButtonText = retrofitRadioButton.text.toString()

        // Getting buttonView
        buttonView = findViewById(R.id.downloadViewButton)
        // Getting circleView
        circleView = findViewById(R.id.circleViewButton)

    }

    // Start Of Methods Related To Notification
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                .apply {
                    lightColor = Color.GREEN
                    enableLights(true)
                }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(downloadStatus: String, fileName: String) : Notification {
        // Getting intent that will open detail activity
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra("FileName",fileName)
        if(downloadStatus == SUCCESS_DOWNLOAD){
            intent.putExtra("Status", "Success")
        }
        else{
            intent.putExtra("Status", "Fail")
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Creating Notification
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Download Status")
            .setContentText(downloadStatus)
            .setSmallIcon(R.drawable.ic_cloud)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_cloud, "Check Status", pendingIntent)
            .setAutoCancel(true)
            .build()

        return notification
    }

    fun showNotification(downloadStatus: String, fileName: String){

        val notification = getNotification(downloadStatus, fileName)
        // Getting Notification Manager
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        // Show Notification
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    // End Of Methods Related To Notification



    // Start Of Methods Related To Downloading
    private fun checkAndStartDownloading(){
        if(downloadRadioGroup.checkedRadioButtonId == -1){
            Toast.makeText(applicationContext, "Please Choose File To Download!", Toast.LENGTH_LONG).show()
        }
        else{
            buttonView.setDownloadingState()
            circleView.setCircleDownloadingState()
            buttonView.isClickable  = false
            when(downloadRadioGroup.checkedRadioButtonId){
                glideRadioButton.id -> downloadFile(GLIDE_URL, glideRadioButtonText)
                appRadioButton.id -> downloadFile(APP_URL, appRadioButtonText)
                retrofitRadioButton.id -> downloadFile(RETROFIT_URL, retrofitRadioButtonText)
            }
        }
    }

    private fun downloadFile(baseURL: String, fileName: String) {

        // Getting the Request to download
        val requestDownload = DownloadManager.Request(Uri.parse(baseURL))
            .setTitle("File Download")
            .setDescription("Downloading File.....")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)

        // Getting downloadManager
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Sending the request to the download manager
        downloadId = downloadManager.enqueue(requestDownload)

        // Getting broadcast receiver
        val broadcastReceiver = object: BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                val id: Long? = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if(id == downloadId){
                    // Download success
                    showNotification(SUCCESS_DOWNLOAD, fileName)
                    buttonView.setSteadyState()
                    buttonView.isClickable = true
                    circleView.setCircleSteadyState()

                }
                else{
                    // Download Failed
                    showNotification(FAILED_DOWNLOAD, fileName)
                }
            }

        }

        // Registering the receiver
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


    }

    // End Of Methods Related to Downloading





}