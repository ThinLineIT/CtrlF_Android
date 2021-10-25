package com.thinlineit.ctrlf.registration.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thinlineit.ctrlf.R

class FindPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FindPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }
}
