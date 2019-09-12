package com.kumail.hintpod.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.snackbar.Snackbar
import com.kumail.hintpod.HintPod.Companion.uniqueFBId
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.adapters.CommentsAdapter
import com.kumail.hintpod.data.Suggestion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.hintpod_activity_suggestion_detailed.*
import kotlinx.android.synthetic.main.hintpod_dialog_add_comment.*


class SuggestionDetailedActivity : AppCompatActivity() {
    private lateinit var suggestion: Suggestion
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hintpod_activity_suggestion_detailed)
        val suggestionTitleTextView = tv_suggestion_title
        val suggestionBodyTextView = tv_suggestion_body
        val suggestionStatusTextView = tv_suggestion_status
        val statusIndicatorImageView = iv_status_indicator
        val upvoteImageView = iv_upvote!!
        val downvoteImageView = iv_downvote!!
        val voteCountTextView = tv_vote_count!!
        val addCommentEditText = et_comment
        val apiService = RetrofitClient().getClient()

        val extras = intent.extras
        if (extras == null) {
            return
        } else {
            suggestion = intent.extras.get("suggestionObject") as Suggestion
            position = intent.extras.get("position") as Int

        }

        suggestionTitleTextView?.text = suggestion.title
        suggestionBodyTextView?.text = suggestion.content

        val status = suggestion.status
        suggestionStatusTextView?.text = status
        statusIndicatorImageView.setImageResource(R.drawable.hintpod_status_indicator)

        if (status == getString(R.string.hintpod_status_pending))
            statusIndicatorImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_yellow))
        else if (status == getString(R.string.hintpod_status_approved))
            statusIndicatorImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_green))
        else
            statusIndicatorImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_red))

        voteCountTextView.text = suggestion.voteCount.toString()

        upvoteImageView.setImageResource(R.drawable.hintpod_ic_arrow_up)
        downvoteImageView.setImageResource(R.drawable.hintpod_ic_arrow_down)

        if (suggestion.vote == getString(R.string.hintpod_true)) {
            upvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_green))

        } else if (suggestion.vote == getString(R.string.hintpod_false)) {
            downvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_red))
        } else {
            upvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_grey))
            downvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_grey))
        }

        upvoteImageView.setOnClickListener {
            if (suggestion.vote == getString(R.string.hintpod_true)) {
                upvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "true", "false")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.vote = null
            } else {
                upvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_green))
                downvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "true", "true")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.vote = getString(R.string.hintpod_true)

            }
            println("Upvote clicked")
        }

        downvoteImageView.setOnClickListener {
            if (suggestion.vote == getString(R.string.hintpod_false)) {
                downvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "false", "false")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.vote = null
            } else {
                upvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_grey))
                downvoteImageView.setColorFilter(ContextCompat.getColor(this, R.color.hintpod_red))
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "false", "true")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.vote = getString(R.string.hintpod_false)

            }
        }

        fab_comments.setOnClickListener {
            MaterialDialog(this, BottomSheet()).show {
                cornerRadius(res = R.dimen.hintpod_add_suggestion_dialog_corner_radius)
                customView(R.layout.hintpod_dialog_add_comment)
                title(R.string.hintpod_add_comment_dialog_title)
                positiveButton(R.string.hintpod_add_suggestion_dialog_submit) { dialog ->
                    // Do something
                    val comment = et_comment.text
                    if (comment.isNotEmpty()) {
                        val responseAddComment = apiService.addComment(comment.toString(), uniqueFBId, suggestion.key)
                        responseAddComment.observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(IoScheduler())
                                .subscribe(
                                        { result ->
                                            println("ADD $result")
                                            println("ADD ${suggestion.key}")
                                            println("ADD $uniqueFBId")
                                            addCommentEditText.text.clear()
                                        },
                                        { error -> println("Error $error") })

                        val snackbar: Snackbar = Snackbar.make(view, "Submitted", Snackbar.LENGTH_LONG)
                        val snackBarView = snackbar.view
                        snackBarView.setBackgroundColor(getColor(R.color.hintpod_colorPrimary))
                        snackbar.show()

                    } else {
                        val snackbar: Snackbar = Snackbar.make(view, "You haven't typed anything...", Snackbar.LENGTH_SHORT)
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

        // Creates a vertical Layout Manager
        rv_comments.layoutManager = LinearLayoutManager(this)

        val responseGetComments = apiService.getComments(suggestion.key)
        responseGetComments.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(IoScheduler())
                .subscribe(
                        { result ->
                            rv_comments.adapter = CommentsAdapter(result, this)
                            println("Here" + result)
                        },
                        { error -> println("Error $error") })

        println("here suggestion detailed act")
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra("updatedSuggestion", suggestion)
        returnIntent.putExtra("position", position)
        println("Mainac on back $suggestion")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }
}