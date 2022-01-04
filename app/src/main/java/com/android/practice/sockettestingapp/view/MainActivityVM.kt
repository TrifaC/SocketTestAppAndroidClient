package com.android.practice.sockettestingapp.view

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.practice.sockettestingapp.data.SocketConnectStatus
import com.android.practice.sockettestingapp.data.SocketEvents
import com.android.practice.sockettestingapp.handlers.SocketHandler
import io.socket.emitter.Emitter

class MainActivityVM(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val LOG_TAG: String = "Main Activity"
    }

    private val mApplication: Application = application

    private lateinit var mSocketHandler: SocketHandler

    private var messageList: ArrayList<String> = ArrayList<String>()
    private var name: String = ""
    private var grade: String = ""
    private var pushUpCount: Int = 0

    private val _stateSocket = MutableLiveData<SocketConnectStatus>()
    val stateSocket: LiveData<SocketConnectStatus>
        get() = _stateSocket

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message


//------------------------------------- Emitter Listener Function ----------------------------------


    /**
     * Method Executes after connecting to socket.
     * */
    private fun connectedCallbackEvent() = Emitter.Listener {
        Log.d(LOG_TAG, "Connect to socket.")
        _stateSocket.postValue(SocketConnectStatus.CONNECTED)
        register(name, grade)
    }

    /**
     * Method Executes after disconnecting to socket.
     * */
    private fun disconnectCallbackEvent() = Emitter.Listener {
        Log.d(LOG_TAG, "Disconnect to socket.")
        _stateSocket.postValue(SocketConnectStatus.DISCONNECTED)
    }

    /**
     * Method Executes after error happens in socket connection.
     * */
    private fun errorConnectCallbackEvent() = Emitter.Listener {
        Log.d(LOG_TAG, "Connect Error")
        _stateSocket.postValue(SocketConnectStatus.ERROR)
    }

    /**
     * Method Executes after receiving on chat event.
     * */
    private fun onChatCallbackEvent(message: String) {
        updateReceiveMessage(message)
    }


//------------------------------------- Initialization ---------------------------------------------


    init {
        initState()
        initMessageList()
    }

    /** Initialize the socket state. */
    private fun initState() {
        _stateSocket.value = SocketConnectStatus.INIT
    }

    /** Initialize the message list. */
    private fun initMessageList() {
        _message.value = ""
    }


//------------------------------------- Event Trigger Functions ------------------------------------


    fun connectSocket(ipAddress: String, name: String, grade: String) {
        // Init Socket.
        mSocketHandler = SocketHandler()
        mSocketHandler.setSocket(ipAddress)
        // Init Socket Emitter Listener.
        mSocketHandler.connectEmitListener = connectedCallbackEvent()
        mSocketHandler.disconnectEmitListener = disconnectCallbackEvent()
        mSocketHandler.errorConnectEmitListener = errorConnectCallbackEvent()
        mSocketHandler.onChatEmitListener =
            Emitter.Listener { args -> onChatCallbackEvent(args[0].toString()) }
        // Bind listener.
        mSocketHandler.bindSocketEmitter()
        // Connect to socket.
        mSocketHandler.connectSocket()
        this.name = name
        this.grade = grade
    }

    fun disconnectSocket() {
        mSocketHandler.disconnectSocket()
        messageList.clear()
        _message.value = ""
    }

    private fun register(nameStr: String, gradeStr: String) {
        if (TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(gradeStr)) {
            mSocketHandler.sendRegisterEvent("Default Name", "Default Grade")
        } else {
            mSocketHandler.sendRegisterEvent(nameStr, gradeStr)
        }
    }

    fun sendMessage(event: SocketEvents, message: String) {
        if (TextUtils.isEmpty(message) && event == SocketEvents.ON_CHAT) {
            mSocketHandler.sendChatEvent(event, "Send Empty")
        } else {
            mSocketHandler.sendChatEvent(event, message)
        }
    }

    fun startSection() {
        mSocketHandler.sendStartEvent()
    }

    fun doSport() {
        pushUpCount += 1
        mSocketHandler.sendPushUpEvent(pushUpCount)
    }


//------------------------------------- Life Cycle Function ----------------------------------------


    /**
     * Function to disconnect socket and unbinding the emit listener.
     * */
    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
        mSocketHandler.unBindSocketEmitter()
    }


//------------------------------------- Data Process -----------------------------------------------


    private fun updateReceiveMessage(message: String) {
        var messageTmp: String = ""
        messageList.add(message)
        for (messageItem in messageList) {
            messageTmp = messageTmp + messageItem + "\n"
        }
        _message.postValue(messageTmp)
    }


}