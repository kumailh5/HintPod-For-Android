package com.kumail.hintpod.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.kumail.hintpod.HintPod.Companion.firebasePId
import com.kumail.hintpod.HintPod.Companion.firebaseUId
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.SuggestionsAdapter
import com.kumail.hintpod.data.Suggestion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.hintpod_activity_main.*
import kotlinx.android.synthetic.main.hintpod_dialog_add_suggestion.view.*


class MainActivity : AppCompatActivity() {

    var suggestionsAdapter: SuggestionsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hintpod_activity_main)
        // Creates a vertical Layout Manager
        rv_suggestions.layoutManager = LinearLayoutManager(this)


        val apiService = RetrofitClient().getClient()

        val responseGet = apiService.getSuggestions(firebasePId, null)
        responseGet.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            println("SIZE")
            println(it.size)
            suggestionsAdapter = SuggestionsAdapter(it, this)
            rv_suggestions.adapter = suggestionsAdapter
            println("MainActivity oncreate")
        }

        fab_suggestions.setOnClickListener { view ->
            val dialogBuilder = AlertDialog.Builder(this)
            // set message of alert dialog
//            dialogBuilder.setMessage("Do you want to add a suggestion?")
//                    // if the dialog is cancelable
//                    .setCancelable(false)
//                    // positive button text and action
//                    .setPositiveButton("Submit", DialogInterface.OnClickListener {
//                        dialog, id -> finish()
//                    })
//                    // negative button text and action
//                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {e
//                        dialog, id -> dialog.cancel()
//                    })

            val dialogView = inflate(this, R.layout.hintpod_dialog_add_suggestion, null)

            dialogBuilder.setView(dialogView)
                    .setCancelable(true)

                    .setPositiveButton("Submit") { dialog, id ->
                        val title = dialogView.et_title
                        val content = dialogView.et_content

                        if (title.text.isNotEmpty()) {
                            println("ADD ${title.text}")

                            val responseAdd = apiService.addSuggestion(title.text.toString(), content.text.toString(), firebaseUId, firebasePId)
                            responseAdd.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
                                println("ADD $it")

                            }

                            Snackbar.make(view, "Submitted", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .show()
                        } else {
                            Snackbar.make(view, "Please enter all fields", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .show()
                        }

                    }
                    // negative button text and action
                    .setNegativeButton("Cancel") { dialog, id ->
                        dialog.cancel()
                    }
            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Add Suggestion")
            // show alert dialog
            alert.show()
        }

        /*
 * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
 * performs a swipe-to-refresh gesture.
 */
        sr_suggestions.setOnRefreshListener {
            println("onRefresh called from SwipeRefreshLayout")

            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            updateData()

        }

    }

    private fun updateData(){
        val responseGet = RetrofitClient().getClient().getSuggestions(firebasePId, null)
        responseGet.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            println("SIZE")
            println(it.size)
            suggestionsAdapter = SuggestionsAdapter(it, this)
            rv_suggestions.adapter = suggestionsAdapter
            println("MainActivity update oncreate")
            sr_suggestions.isRefreshing = false

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val suggestion = data?.getSerializableExtra("updatedSuggestion") as Suggestion
                val position = data.getSerializableExtra("position") as Int
                println("MainAct $suggestion")
                suggestionsAdapter?.updateAdapter(suggestion, position)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                println("MainActivity no result code")
            }
        }

    }//onActivityResult

}
