package com.kumail.hintpod.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.snackbar.Snackbar
import com.kumail.hintpod.HintPod.Companion.companyFBId
import com.kumail.hintpod.HintPod.Companion.projectFBId
import com.kumail.hintpod.HintPod.Companion.uniqueFBId
import com.kumail.hintpod.HintPod.Companion.userTheme
import com.kumail.hintpod.HintPod.Companion.userTitle
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.SuggestionsAdapter
import com.kumail.hintpod.data.Suggestion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.hintpod_activity_main.*
import kotlinx.android.synthetic.main.hintpod_dialog_add_suggestion.*


//import sun.jvm.hotspot.utilities.IntArray


class MainActivity : AppCompatActivity() {

    var suggestionsAdapter: SuggestionsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (userTheme != 0) {
            setTheme(userTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hintpod_activity_main)
        // Creates a vertical Layout Manager
        rv_suggestions.layoutManager = LinearLayoutManager(this)

        val apiService = RetrofitClient().getClient()

        progress_bar.display

        if (userTitle != null) {
            title = userTitle
        }

        tv_hintpod_powered_by.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.hintpod_redirecting_title)
                message(R.string.hintpod_redirecting_message)

                positiveButton(R.string.hintpod_redirecting_yes) { dialog ->
                    // Do something
                    val url = getString(R.string.hintpod_url)
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
                negativeButton(R.string.hintpod_redirecting_cancel) { dialog ->
                    // Do something
                    dismiss()
                }
            }


        }

        val responseGet = apiService.getSuggestions(companyFBId, projectFBId, null)
        responseGet.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(IoScheduler())
                .subscribe(
                        { result ->
                            println("SIZE")
                            println(result.size)
                            suggestionsAdapter = SuggestionsAdapter(result, this)
                            rv_suggestions.adapter = suggestionsAdapter
                            progress_bar.visibility = View.GONE
                        },
                        { error -> println("Error $error") })



        fab_suggestions.setOnClickListener {
            MaterialDialog(this, BottomSheet()).show {
                cornerRadius(res = R.dimen.hintpod_add_suggestion_dialog_corner_radius)
                customView(R.layout.hintpod_dialog_add_suggestion)
                title(R.string.hintpod_add_suggestion_dialog_title)
                positiveButton(R.string.hintpod_add_suggestion_dialog_submit) { dialog ->
                    // Do something
                    val title = et_title.text
                    val content = et_content.text

                    if (title.isNotEmpty()) {

                        val responseAdd = apiService.addSuggestion(title.toString(), content.toString(), uniqueFBId, projectFBId)
                        responseAdd.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler())
                                .subscribe(
                                        { result ->
                                            println("ADD $result")
                                        },
                                        { error -> println("Error $error") })

                        val snackbar: Snackbar = Snackbar.make(view, "Submitted", Snackbar.LENGTH_LONG)
                        val snackBarView = snackbar.view
                        snackBarView.setBackgroundColor(getColor(R.color.hintpod_colorPrimary))
                        snackbar.show()


                    } else {
                        val snackbar: Snackbar = Snackbar.make(view, "Please type something...", Snackbar.LENGTH_SHORT)
                        val snackBarView = snackbar.view
                        snackBarView.setBackgroundColor(getColor(R.color.hintpod_orange))
                        snackbar.show()
                        noAutoDismiss()

                    }
                }
                negativeButton(R.string.hintpod_add_suggestion_dialog_cancel) { dialog ->
                    dialog.cancel()
                    // Do something

                    val snackbar: Snackbar = Snackbar.make(view, "Cancelled", Snackbar.LENGTH_LONG)
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(getColor(R.color.hintpod_red_dark))
                    snackbar.show()

                }

            }
        }

        /*
 * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
 * performs a swipe-to-refresh gesture.
 */
        sr_suggestions.setOnRefreshListener {
            println("onRefresh called from SwipeRefreshLayout")
            // This method performs the actual data-refresh operation.
            updateData()
        }

    }

    private fun updateData() {
        val responseGet = RetrofitClient().getClient().getSuggestions(companyFBId, projectFBId, null)
        responseGet.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(IoScheduler())
                .subscribe(
                        { result ->
                            println("SIZE")
                            println(result.size)
                            suggestionsAdapter = SuggestionsAdapter(result, this)
                            rv_suggestions.adapter = suggestionsAdapter
                            println("MainActivity update oncreate")
                            sr_suggestions.isRefreshing = false
                        },
                        { error -> println("Error $error") })
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
