package com.example.kotlinhomework6

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinhomework6.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapOf(
            binding.btnBooks to BooksActivity::class.java,
            binding.btnBooksMaster to BooksMasterActivity::class.java,
            binding.btnBooksSlave to BooksSlaveActivity::class.java,
        ).forEach { (button, activity) ->
            button.setOnClickListener {
                Intent(this@MainActivity, activity).also {
                    startActivity(it)
                }
            }
        }

        binding.btnAirQuality.setOnClickListener {
            binding.btnAirQuality.isEnabled = false
            sendRequest()
        }
    }

    private fun sendRequest() {
        val url = "https://api.italkutalk.com/api/air"
        val req = Request.Builder()
            .url(url)
            .build()

        OkHttpClient().newCall(req).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val airQualityObject = Gson().fromJson(json, AirQualityObject::class.java)
                showDialog(airQualityObject)
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    binding.btnAirQuality.isEnabled = true
                    Toast.makeText(
                        this@MainActivity,
                        "查詢失敗$e", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun showDialog(airQualityObject: AirQualityObject) {
        val items = mutableListOf<String>()
        airQualityObject.result.records.forEach { data ->
            items.add("地區：${data.SiteName}, 狀態：${data.Status}")
        }
        runOnUiThread {
            binding.btnAirQuality.isEnabled = true
            AlertDialog.Builder(this@MainActivity)
                .setTitle("臺北市空氣品質")
                .setItems(items.toTypedArray(), null)
                .show()
        }
    }
}
