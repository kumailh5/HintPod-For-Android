package com.kumail.showme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.suggestion_item.view.*

class SuggestionsAdapter(val items : ArrayList<String>, val body : ArrayList<String>, val status : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.suggestion_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSuggestionTitle?.text = items.get(position)
        holder.tvSuggestionBody?.text = body.get(0)
        holder.tvSuggestionStatus?.text = status.get(0)


//        Picasso
//                .get() // give it the context
//                .load("https://avatars2.githubusercontent.com/u/1442761?s=88&v=4") // load the image
//                .into(holder.ivStatusIndicator) // select the ImageView to load it into

    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvSuggestionTitle = view.tv_suggestion_title
    val tvSuggestionBody = view.tv_suggestion_body
    val tvSuggestionStatus = view.tv_suggestion_status
    val ivStatusIndicator = view.iv_status_indicator
}
