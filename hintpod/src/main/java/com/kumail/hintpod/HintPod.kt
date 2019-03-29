package com.kumail.hintpod

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.kumail.hintpod.activities.MainActivity

class HintPod {
    fun start(context: Context){
        Toast.makeText(context, "Here's hintpod", Toast.LENGTH_LONG).show()
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
    }
}