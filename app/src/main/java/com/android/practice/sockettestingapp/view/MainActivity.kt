package com.android.practice.sockettestingapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.practice.sockettestingapp.R
import com.android.practice.sockettestingapp.data.SocketConnectStatus
import com.android.practice.sockettestingapp.data.SocketEvents
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
        binding.disconnectBTN.isEnabled = false
        binding.sendBTN.isEnabled = false
    }

    private fun initClickListener() {
        binding.sendBTN.setOnClickListener {
            val message: String = binding.messageET.text.toString().trim()
            mainViewModel.sendMessage(SocketEvents.ON_CHAT, message)
            binding.messageET.setText("")
        }
        binding.connectBTN.setOnClickListener {
            val name: String = binding.nameET.text.toString().trim()
            val grade: String = binding.gradeET.text.toString().trim()
            val ipAddress: String = binding.ipET.text.toString().trim()
            mainViewModel.connectSocket(ipAddress, name, grade)
            binding.ipET.setText("")
            binding.nameET.setText("")
            binding.gradeET.setText("")
        }
        binding.disconnectBTN.setOnClickListener {
            mainViewModel.disconnectSocket()
        }
        binding.startBTN.setOnClickListener {
            mainViewModel.startSection()
        }
        binding.pushUpBTN.setOnClickListener {
            mainViewModel.doSport()
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
        binding.sendBTN.isEnabled = true
        binding.disconnectBTN.isEnabled = true
        binding.messageET.isEnabled = true
        binding.startBTN.isEnabled = true
        binding.pushUpBTN.isEnabled = true
        binding.connectBTN.isEnabled = false
        binding.nameET.isEnabled = false
        binding.gradeET.isEnabled = false
        binding.ipET.isEnabled = false
    }

    private fun enableBtnsInDisconnectAndError() {
        binding.sendBTN.isEnabled = false
        binding.disconnectBTN.isEnabled = false
        binding.messageET.isEnabled = false
        binding.startBTN.isEnabled = false
        binding.pushUpBTN.isEnabled = false
        binding.connectBTN.isEnabled = true
        binding.nameET.isEnabled = true
        binding.gradeET.isEnabled = true
        binding.ipET.isEnabled = true

    }


}