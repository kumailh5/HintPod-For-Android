package com.kumail.hintpod

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.kumail.hintpod.activities.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler


class HintPod : Application() {

    companion object {
        var uniqueFBId: String = ""
        var projectFBId: String = ""
        var companyFBId: String = ""
    }

    fun start(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(context, intent, null)
    }

    fun verify(uniqueId: String, projectId: String, apikey: String, name: String?) {
        projectFBId = projectId
        companyFBId = apikey
        val apiService = RetrofitClient().getClient()
        val response = apiService.verifyUser(uniqueId, projectId, name)
        response.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(IoScheduler())
                .subscribe(
                        { result ->
                            println("verified")
                            uniqueFBId = result.toString() },
                        { error -> println("Error $error") })
    }
}
