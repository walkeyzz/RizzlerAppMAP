package com.projectmap.rizzler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectmap.rizzler.databinding.FragmentEditProfileBinding

class AccountSettings : AppCompatActivity() {
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logOutBtn.setOnClickListener {
            startActivity(Intent(this@AccountSettings, LoginActivity::class.java))
            finish()
        }
    }
}
