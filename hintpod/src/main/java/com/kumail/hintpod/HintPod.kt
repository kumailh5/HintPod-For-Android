package com.kumail.hintpod

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.kumail.hintpod.activities.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler


class HintPod : Application() {

    companion object {
        var firebaseUId: String = ""
        var firebasePId: String = ""
    }

    fun start(context: Context) {
        Toast.makeText(context, "Here's HintPod", Toast.LENGTH_LONG).show()
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
    }

    fun verify(uniqueId: String, projectId: String, name: String?) {
        firebasePId = projectId
        val apiService = RetrofitClient().getClient()
        val response = apiService.verifyUser(uniqueId, projectId, name)
        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            firebaseUId = it.toString()
        }
    }
}
