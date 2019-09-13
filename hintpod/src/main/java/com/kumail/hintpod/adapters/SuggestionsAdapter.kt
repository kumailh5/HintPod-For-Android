package com.kumail.hintpod.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kumail.hintpod.HintPod.Companion.uniqueFBId
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.activities.SuggestionDetailedActivity
import com.kumail.hintpod.data.Suggestion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.hintpod_item_suggestion.view.*
import java.io.Serializable


class SuggestionsAdapter(val suggestionsList: List<Suggestion>, val context: Context) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return suggestionsList.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.hintpod_item_suggestion, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val suggestion = payloads[0] as Suggestion
            if (suggestion.vote == context.getString(R.string.hintpod_true)) {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_green))
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))

            } else if (suggestion.vote == context.getString(R.string.hintpod_false)) {
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_red))
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))

            } else {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))

            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, SuggestionDetailedActivity::class.java)
                intent.putExtra("suggestionObject", suggestion as Serializable)
                intent.putExtra("position", position)
                (context as Activity).startActivityForResult(intent, 1)
            }
            println("MainAc bindview sadapter $suggestion")
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apiService = RetrofitClient().getClient()
        val suggestion = suggestionsList[position]

        holder.suggestionTitleTextView.text = suggestion.title
        holder.suggestionBodyTextView.text = suggestion.content

        val status = suggestion.status
        holder.suggestionStatusTextView.text = status
        holder.statusIndicatorImageView.setImageResource(R.drawable.hintpod_status_indicator)

        if (status == context.getString(R.string.hintpod_status_pending))
            holder.statusIndicatorImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_yellow))
        else if (status == context.getString(R.string.hintpod_status_approved))
            holder.statusIndicatorImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_green))
        else
            holder.statusIndicatorImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_red))

        holder.upvoteImageView.setImageResource(R.drawable.hintpod_ic_arrow_up)
        holder.downvoteImageView.setImageResource(R.drawable.hintpod_ic_arrow_down)

        var vote = suggestion.votes[uniqueFBId]
        println("votearst " + vote)
        suggestion.vote = vote

        holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
        holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))

        vote?.let {
            println("votes this runs " + vote)
            if (vote == context.getString(R.string.hintpod_true)) {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_green))
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))

            } else if (vote == context.getString(R.string.hintpod_false)) {
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_red))
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))

            } else {
                vote = null
            }
            suggestion.vote = vote

        }

        holder.upvoteImageView.setOnClickListener {
            if (vote == context.getString(R.string.hintpod_true)) {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "true", "false")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.votes.remove(uniqueFBId)
                notifyItemChanged(position, suggestion)
                vote = null

            } else {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_green))
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "true", "true")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.votes[uniqueFBId] = context.getString(R.string.hintpod_true)
                notifyItemChanged(position, suggestion)
                vote = context.getString(R.string.hintpod_true)

            }
            suggestion.vote = vote
        }

        holder.downvoteImageView.setOnClickListener {
            if (vote == context.getString(R.string.hintpod_false)) {
                holder.upvoteImageView.setColorFilter(R.color.hintpod_grey)
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "true", "false")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.votes.remove(uniqueFBId)
                notifyItemChanged(position, suggestion)
                vote = null
            } else {
                holder.upvoteImageView.setColorFilter(R.color.hintpod_grey)
                holder.downvoteImageView.setColorFilter(R.color.hintpod_red)
                val responseVoteSuggestion = apiService.voteSuggestion(uniqueFBId, suggestion.key, "false", "true")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(IoScheduler())
                        .subscribe(
                                { result -> println("Vote $result") },
                                { error -> println("Error $error") })
                suggestion.votes[uniqueFBId] = context.getString(R.string.hintpod_false)
                notifyItemChanged(position, suggestion)
                vote = context.getString(R.string.hintpod_false)
            }
            suggestion.vote = vote

        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SuggestionDetailedActivity::class.java)
            intent.putExtra("suggestionObject", suggestion as Serializable)
            intent.putExtra("position", position)
            (context as Activity).startActivityForResult(intent, 1)
        }
    }

    fun updateAdapter(suggestion: Suggestion, position: Int) {
        notifyItemChanged(position, suggestion)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add information to
        val suggestionTitleTextView = view.tv_suggestion_title!!
        val suggestionBodyTextView = view.tv_suggestion_body!!
        val suggestionStatusTextView = view.tv_suggestion_status!!
        val statusIndicatorImageView = view.iv_status_indicator!!
        val upvoteImageView = view.iv_upvote!!
        val downvoteImageView = view.iv_downvote!!
    }

}


