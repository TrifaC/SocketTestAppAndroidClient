package com.android.practice.sockettestingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import io.socket.emitter.Emitter

import org.json.JSONException

import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG: String = "MainActivity"

    }

    private lateinit var sendButton: Button
    private lateinit var messageEditText: EditText
    private lateinit var messageTextView: TextView

    private lateinit var mSocketHandler: SocketHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSocket()
        initUI()
        initClickListener()
    }


//------------------------------------- Initialization ---------------------------------------------


    private fun initSocket() {
        mSocketHandler = SocketHandler()
        mSocketHandler.setSocket(Constants.URI_STRING_LOCAL)
        mSocketHandler.establishSocket()
    }


    private fun initUI() {
        sendButton = findViewById(R.id.send_button)
        messageEditText = findViewById(R.id.message_edit_text)
        messageTextView = findViewById(R.id.message_TV)
    }

    private fun initClickListener() {
        sendButton.setOnClickListener {
            val message: String = messageEditText.text.toString().trim()
            if (TextUtils.isEmpty(message)) {
                mSocketHandler.sendMessage(SocketEvents.ON_CHAT, "Send Empty")
            } else {
                messageEditText.setText("")
                mSocketHandler.sendMessage(SocketEvents.ON_CHAT, message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocketHandler.disconnectSocket()
    }
}