package com.android.practice.sockettestingapp.view

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import com.android.practice.sockettestingapp.data.SocketEvents
import com.android.practice.sockettestingapp.handlers.SocketHandler
import com.android.practice.sockettestingapp.util.Constants

class MainActivityVM(application: Application): AndroidViewModel(application) {
    companion object {
        private const val LOG_TAG: String = "Main Activity"
    }

    private val mApplication: Application = application

    private lateinit var mSocketHandler: SocketHandler


//------------------------------------- Initialization ---------------------------------------------

    init {
        initSocket()
    }

    private fun initSocket() {
        mSocketHandler = SocketHandler()
        mSocketHandler.setSocket(Constants.URI_STRING_LOCAL)
    }


//------------------------------------- Event Trigger Functions ------------------------------------


    fun connectSocket(){
        mSocketHandler.connectSocket()
    }

    fun sendMessage(message: String) {
        if (TextUtils.isEmpty(message)) {
            mSocketHandler.sendMessage(SocketEvents.ON_CHAT, "Send Empty")
        } else {
            mSocketHandler.sendMessage(SocketEvents.ON_CHAT, message)
        }
    }

    fun disconnectSocket() {
        mSocketHandler.disconnectSocket()
    }


//------------------------------------- Life Cycle Function ----------------------------------------


    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }
}