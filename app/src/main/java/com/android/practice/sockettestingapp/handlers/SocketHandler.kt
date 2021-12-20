package com.android.practice.sockettestingapp.handlers

import android.util.Log
import com.android.practice.sockettestingapp.data.SocketEvents
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException

class SocketHandler {

    companion object {
        const val LOG_TAG = "SocketHandler"
    }

    private lateinit var  mSocket: Socket

    lateinit var connectEmitListener: Emitter.Listener
    lateinit var disconnectEmitListener: Emitter.Listener
    lateinit var errorConnectEmitListener: Emitter.Listener
    lateinit var onChatEmitListener: Emitter.Listener


//------------------------------------- Emit Listener ----------------------------------------------


    /**
     * Binding all socket emitters.
     * */
    fun bindSocketEmitter() {
        mSocket.on(Socket.EVENT_CONNECT, connectEmitListener)
        mSocket.on(Socket.EVENT_DISCONNECT, disconnectEmitListener)
        mSocket.on(Socket.EVENT_CONNECT_ERROR, errorConnectEmitListener)
        mSocket.on(SocketEvents.ON_CHAT.eventStr, onChatEmitListener)
    }

    /**
     * Unbinding all socket emitters.
     * */
    fun unBindSocketEmitter() {
        mSocket.off(Socket.EVENT_CONNECT, connectEmitListener)
        mSocket.off(Socket.EVENT_DISCONNECT, disconnectEmitListener)
        mSocket.off(Socket.EVENT_CONNECT_ERROR, errorConnectEmitListener)
        mSocket.off(SocketEvents.ON_CHAT.eventStr, onChatEmitListener)
    }


//------------------------------------- Socket Handle ----------------------------------------------


    @Synchronized
    fun setSocket(uriString: String) {
        try {
            mSocket = IO.socket(uriString)
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
    }


//------------------------------------- Message Handler --------------------------------------------


    fun sendMessage(message: String) {
        mSocket.emit(SocketEvents.ON_CHAT.eventStr, message)
    }

    fun register(nameStr: String, gradeStr: String) {
        val jsonObject = JSONObject("""{"name": "$nameStr", "grade": "$gradeStr"}""")
        mSocket.emit(SocketEvents.REGISTER.eventStr, jsonObject)
    }


}