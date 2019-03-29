package com.kumail.hintpod.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kumail.hintpod.R
import com.kumail.hintpod.data.Suggestion
import com.kumail.hintpod.activities.SuggestionDetailedActivity
import kotlinx.android.synthetic.main.item_suggestion.view.*

class SuggestionsAdapter(val suggestionsList: List<Suggestion>, val context: Context) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return suggestionsList.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.suggestionTitleTextView?.text = suggestionsList[position].title
        holder.suggestionBodyTextView?.text = suggestionsList[position].content
        val status = suggestionsList[position].status
        holder.suggestionStatusTextView?.text = status
        if(status == "Pending")
            holder.statusIndicatorImageView.setImageResource(R.drawable.status_indicator_pending)
        else if(status == "Done")
            holder.statusIndicatorImageView.setImageResource(R.drawable.status_indicator_approved)
        else
            holder.statusIndicatorImageView.setImageResource(R.drawable.status_indicator_rejected)


        holder.itemView.setOnClickListener {
            val suggestionId = suggestionsList[position].key

            println("suggestion adapter: $suggestionId")

            val intent = Intent(context, SuggestionDetailedActivity::class.java)
            intent.putExtra("suggestionId", suggestionId)
//            intent.putExtra("suggestionId", suggestionsList[position])
            ContextCompat.startActivity(context, intent, null)
//            context.startActivity(intent)


        }
//        Picasso
//                .get() // give it the context
//                .load("https://avatars2.githubusercontent.com/u/1442761?s=88&v=4") // load the image
//                .into(holder.ivStatusIndicator) // select the ImageView to load it into

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add information to
        val suggestionTitleTextView = view.tv_suggestion_title
        val suggestionBodyTextView = view.tv_suggestion_body
        val suggestionStatusTextView = view.tv_suggestion_status
        val statusIndicatorImageView = view.iv_status_indicator
    }
}


