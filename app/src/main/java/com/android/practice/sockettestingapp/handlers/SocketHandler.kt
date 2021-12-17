package com.android.practice.sockettestingapp.handlers

import android.util.Log
import com.android.practice.sockettestingapp.data.SocketEvents
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketHandler {

    companion object {
        const val LOG_TAG = "SocketHandler"
    }

    lateinit var  mSocket: Socket
    private lateinit var connectEmitListener: Emitter.Listener
    private lateinit var disconnectEmitListener: Emitter.Listener
    private lateinit var errorConnectEmitListener: Emitter.Listener
    private lateinit var onChatEmitListener: Emitter.Listener


//------------------------------------- Initialization ---------------------------------------------


    private fun initEmitterListener(socket: Socket) {
        connectEmitListener = Emitter.Listener { Log.d(LOG_TAG, "Emit Listener: Connected." ) }
        disconnectEmitListener = Emitter.Listener { Log.d(LOG_TAG, "Emit Listener: Disconnected.") }
        errorConnectEmitListener = Emitter.Listener { Log.d(LOG_TAG, "Emit Listener: Connect Error.") }
        onChatEmitListener = Emitter.Listener { args -> Log.d(LOG_TAG, "The message is ${args[0]}") }

        socket.on(Socket.EVENT_CONNECT, connectEmitListener)
        socket.on(Socket.EVENT_DISCONNECT, disconnectEmitListener)
        socket.on(Socket.EVENT_CONNECT_ERROR, errorConnectEmitListener)
        socket.on(SocketEvents.ON_CHAT.eventStr, onChatEmitListener)
    }


//------------------------------------- Socket Handle ----------------------------------------------


    @Synchronized
    fun setSocket(uriString: String) {
        try {
            mSocket = IO.socket(uriString)
            initEmitterListener(mSocket)
        } catch (error: URISyntaxException) {
            Log.d(LOG_TAG, "Cannot connect to socket.")
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun connectSocket() {
        mSocket.connect()
    }

    @Synchronized
    fun disconnectSocket() {
        mSocket.disconnect()
        mSocket.off(Socket.EVENT_CONNECT, connectEmitListener)
        mSocket.off(Socket.EVENT_DISCONNECT, disconnectEmitListener)
        mSocket.off(Socket.EVENT_CONNECT_ERROR, errorConnectEmitListener)
    }


//------------------------------------- Message Handler --------------------------------------------


    fun sendMessage(events: SocketEvents, message: String) {
        mSocket.emit(events.eventStr, message)
    }

}