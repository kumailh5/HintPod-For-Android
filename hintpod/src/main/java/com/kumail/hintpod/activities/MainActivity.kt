package com.kumail.hintpod.activities

import android.os.Bundle
import android.view.View
import android.view.View.inflate
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kumail.hintpod.HintPod.Companion.firebasePId
import com.kumail.hintpod.HintPod.Companion.firebaseUId
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.SuggestionsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.hintpod_activity_main.*
import kotlinx.android.synthetic.main.hintpod_dialog_add_suggestion.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hintpod_activity_main)

        // Creates a vertical Layout Manager
        rv_suggestions.layoutManager = LinearLayoutManager(this)

        val apiService = RetrofitClient().getClient()
        val responseGet = apiService.getSuggestions()
        responseGet.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            println("SIZE")
            println(it.size)
            rv_suggestions.adapter = SuggestionsAdapter(it, this)

        }

        val fab: View = findViewById(R.id.fab_suggestions)
        fab.setOnClickListener { view ->


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
//                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
//                        dialog, id -> dialog.cancel()
//                    })

            val dialogView = inflate(this, R.layout.hintpod_dialog_add_suggestion, null)

            dialogBuilder.setView(dialogView)
                    .setCancelable(true)

                    .setPositiveButton("Submit") { dialog, id ->


                        val title = dialogView.et_title
                        val content = dialogView.et_content

                        if (title.text.isNotEmpty()) {

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
                            println("ADD Empty")

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
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            // Get the layout inflater
//            val inflater = requireActivity().layoutInflater;
//
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//            builder.setView(inflater.inflate(R.layout.hintpod_dialog_add_suggestion, null))
//                    // Add action buttons
//                    .setPositiveButton("ok",
//                            DialogInterface.OnClickListener { dialog, id ->
//                                // sign in the user ...
//                            })
//                    .setNegativeButton("no",
//                            DialogInterface.OnClickListener { dialog, id ->
//                                getDialog().cancel()
//                            })
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }

}
