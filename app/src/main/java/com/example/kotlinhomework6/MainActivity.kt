package com.example.kotlinhomework6

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlinhomework6.databinding.ActivityMainBinding

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
    }
}
