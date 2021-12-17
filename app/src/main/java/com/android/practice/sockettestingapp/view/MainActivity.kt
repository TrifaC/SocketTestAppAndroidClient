package com.android.practice.sockettestingapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.practice.sockettestingapp.R
import com.android.practice.sockettestingapp.data.SocketEvents
import com.android.practice.sockettestingapp.databinding.ActivityMainBinding
import com.android.practice.sockettestingapp.handlers.SocketHandler
import com.android.practice.sockettestingapp.util.Constants


class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG: String = "MainActivity"

    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainActivityVM
    private lateinit var mainViewModelFactory: MainActivityVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUIAndVM()
        initClickListener()
    }


//------------------------------------- Initialization ---------------------------------------------


    private fun initUIAndVM() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModelFactory = MainActivityVMFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainActivityVM::class.java)
        binding.lifecycleOwner = this

    }

    private fun initClickListener() {
        binding.sendButton.setOnClickListener {
            val message: String = binding.messageEditText.text.toString().trim()
            mainViewModel.sendMessage(message)
            binding.messageEditText.setText("")
        }
        binding.connectButton.setOnClickListener {
            mainViewModel.connectSocket()
        }
        binding.disconnectButton.setOnClickListener {
            mainViewModel.disconnectSocket()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}