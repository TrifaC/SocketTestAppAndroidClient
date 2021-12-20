package com.android.practice.sockettestingapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.practice.sockettestingapp.R
import com.android.practice.sockettestingapp.data.SocketConnectStatus
import com.android.practice.sockettestingapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG: String = "MainActivity"

    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainActivityVM
    private lateinit var mainViewModelFactory: MainActivityVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUIAndVM()
        initClickListener()
        initConnection()
    }


//------------------------------------- Initialization ---------------------------------------------


    private fun initUIAndVM() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModelFactory = MainActivityVMFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainActivityVM::class.java)
        binding.lifecycleOwner = this

        // Disable the button when the state is Init.
        binding.disconnectButton.isEnabled = false
        binding.sendButton.isEnabled = false
    }

    private fun initClickListener() {
        binding.sendButton.setOnClickListener {
            val message: String = binding.messageET.text.toString().trim()
            mainViewModel.sendMessage(message)
            binding.messageET.setText("")
        }
        binding.connectButton.setOnClickListener {
            val name: String = binding.nameET.text.toString().trim()
            val grade: String = binding.gradeET.text.toString().trim()
            mainViewModel.connectSocket(name, grade)
            binding.nameET.setText("")
            binding.gradeET.setText("")

        }
        binding.disconnectButton.setOnClickListener {
            mainViewModel.disconnectSocket()
        }
    }

    private fun initConnection() {
        mainViewModel.message.observe(this, Observer { binding.messageTV.text = it })
        mainViewModel.stateSocket.observe(this, Observer {
            when(it) {
                SocketConnectStatus.CONNECTED -> { enableBtnsInConnect() }
                else -> { enableBtnsInDisconnectAndError() }
            }
        })
    }


//------------------------------------- UI Update Function -----------------------------------------


    private fun enableBtnsInConnect() {
        binding.sendButton.isEnabled = true
        binding.disconnectButton.isEnabled = true
        binding.messageET.isEnabled = true
        binding.connectButton.isEnabled = false
        binding.nameET.isEnabled = false
        binding.gradeET.isEnabled = false
    }

    private fun enableBtnsInDisconnectAndError() {
        binding.sendButton.isEnabled = false
        binding.disconnectButton.isEnabled = false
        binding.messageET.isEnabled = false
        binding.connectButton.isEnabled = true
        binding.nameET.isEnabled = true
        binding.gradeET.isEnabled = true

    }


}