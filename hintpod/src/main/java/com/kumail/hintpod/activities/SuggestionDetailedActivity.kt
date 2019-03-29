package com.kumail.hintpod.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.CommentsAdapter
import com.kumail.hintpod.data.Suggestion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_suggestion_detailed.*

class SuggestionDetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion_detailed)
        val suggestionTitleTextView = tv_suggestion_title
        val suggestionBodyTextView = tv_suggestion_body
        val suggestionStatusTextView = tv_suggestion_status
        val statusIndicatorImageView = iv_status_indicator

        val suggestion: Suggestion
        val extras = intent.extras
        if (extras == null) {
            return
        } else {
            suggestion = intent.extras.get("suggestionObject") as Suggestion

            println("DETAILS ${suggestion.key}")
        }

        suggestionTitleTextView?.text = suggestion.title
        suggestionBodyTextView?.text = suggestion.content

        val status = suggestion.status
        suggestionStatusTextView?.text = status
        println("Pending " + getString(R.string.status_pending))
        if (status == getString(R.string.status_pending))
            statusIndicatorImageView.setImageResource(R.drawable.status_indicator_pending)
        else if (status == getString(R.string.status_approved))
            statusIndicatorImageView.setImageResource(R.drawable.status_indicator_approved)
        else
            statusIndicatorImageView.setImageResource(R.drawable.status_indicator_rejected)


        // Creates a vertical Layout Manager
        rv_comments.layoutManager = LinearLayoutManager(this)

        val apiService = RetrofitClient().getClient()
        val response = apiService.getComments(suggestion.key)
        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            rv_comments.adapter = CommentsAdapter(it, this)
        }


        println("here suggestion detailed act")
    }

}