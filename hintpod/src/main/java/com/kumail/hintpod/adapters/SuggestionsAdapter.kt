package com.kumail.hintpod.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kumail.hintpod.HintPod.Companion.firebaseUId
import com.kumail.hintpod.R
import com.kumail.hintpod.RetrofitClient
import com.kumail.hintpod.activities.SuggestionDetailedActivity
import com.kumail.hintpod.data.Suggestion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.hintpod_item_suggestion.view.*
import java.io.Serializable


class SuggestionsAdapter(val suggestionsList: List<Suggestion>, val context: Context) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    var suggestionsLis: ArrayList<Suggestion> = suggestionsList as ArrayList<Suggestion>

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
                if (suggestion.upEnabled) {
                    holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_green))
                    holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))

                } else if (suggestion.downEnabled) {
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
            println("MainAc bindview $suggestion")
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

        holder.voteCountTextView.text = suggestion.voteCount.toString()

        holder.upvoteImageView.setImageResource(R.drawable.hintpod_ic_arrow_up)
        holder.downvoteImageView.setImageResource(R.drawable.hintpod_ic_arrow_down)

        for ((key, value) in suggestion.votes) {
            if (key == firebaseUId && value == "true") {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_green))
                suggestion.upEnabled = true
            } else if (key == firebaseUId && value != "true") {
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_red))
                suggestion.downEnabled = true
            }
        }

        holder.upvoteImageView.setOnClickListener {
            if (suggestion.upEnabled) {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(firebaseUId, suggestion.key, "true", "false")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
                    println("Vote: $it")
                }
                suggestion.upEnabled = false
                notifyItemChanged(position, suggestion)

            } else {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_green))
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(firebaseUId, suggestion.key, "true", "true")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
                    println("Vote: $it")
                }
                suggestion.upEnabled = true
                suggestion.downEnabled = false
                notifyItemChanged(position, suggestion)

            }
        }

        holder.downvoteImageView.setOnClickListener {
            if (suggestion.downEnabled) {
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
                val responseVoteSuggestion = apiService.voteSuggestion(firebaseUId, suggestion.key, "false", "false")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
                    println("Vote: $it")
                }
                suggestion.downEnabled = false
                notifyItemChanged(position, suggestion)
            } else {
                holder.upvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_grey))
                holder.downvoteImageView.setColorFilter(ContextCompat.getColor(context, R.color.hintpod_red))
                val responseVoteSuggestion = apiService.voteSuggestion(firebaseUId, suggestion.key, "false", "true")
                responseVoteSuggestion.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
                    println("Vote: $it")
                }
                suggestion.downEnabled = true
                suggestion.upEnabled = false
                notifyItemChanged(position, suggestion)
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SuggestionDetailedActivity::class.java)
            intent.putExtra("suggestionObject", suggestion as Serializable)
            intent.putExtra("position", position)
//            startActivity(context, intent, null)null)
            (context as Activity).startActivityForResult(intent, 1)
        }

        println("MainAc bindview $suggestion")
    }

    fun updateAdapter(suggestion: Suggestion, position: Int) {
        println("MainAc adpater")
        println("Mainac $suggestion")
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
        val voteCountTextView = view.tv_vote_count!!
    }

}


