package com.kumail.hintpod.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kumail.hintpod.HintPod.Companion.firebaseUId
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.CommentsAdapter
import com.kumail.hintpod.data.Suggestion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.hintpod_activity_suggestion_detailed.*

class SuggestionDetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hintpod_activity_suggestion_detailed)
        val suggestionTitleTextView = tv_suggestion_title
        val suggestionBodyTextView = tv_suggestion_body
        val suggestionStatusTextView = tv_suggestion_status
        val statusIndicatorImageView = iv_status_indicator
        val addCommentEditText = et_add_comment
        val apiService = RetrofitClient().getClient()

        val suggestion: Suggestion
        val extras = intent.extras
        if (extras == null) {
            return
        } else {
            suggestion = intent.extras.get("suggestionObject") as Suggestion
        }

        suggestionTitleTextView?.text = suggestion.title
        suggestionBodyTextView?.text = suggestion.content

        val status = suggestion.status
        suggestionStatusTextView?.text = status
        if (status == getString(R.string.hintpod_status_pending))
            statusIndicatorImageView.setImageResource(R.drawable.hintpod_status_indicator_pending)
        else if (status == getString(R.string.hintpod_status_approved))
            statusIndicatorImageView.setImageResource(R.drawable.hintpod_status_indicator_approved)
        else
            statusIndicatorImageView.setImageResource(R.drawable.hintpod_status_indicator_rejected)

//        addCommentEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                println(message = "Final Number is $s")
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                println(message = "Number is $s")
//            }
//
//        })

        addCommentEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code

                val s = addCommentEditText.text
                println("Comment $s")

                val responseAddComment = apiService.addComment(addCommentEditText.text.toString(), firebaseUId, suggestion.key)
                responseAddComment.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
                    println("ADD $it")
                    println("ADD ${suggestion.key}")
                    println("ADD $firebaseUId")

                    Snackbar.make(v, "Submitted", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show()

                }
                return@OnKeyListener true
            }
            false
        })

        // Creates a vertical Layout Manager
        rv_comments.layoutManager = LinearLayoutManager(this)

        val responseGetComments = apiService.getComments(suggestion.key)
        responseGetComments.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            rv_comments.adapter = CommentsAdapter(it, this)
        }

        println("here suggestion detailed act")
    }

}