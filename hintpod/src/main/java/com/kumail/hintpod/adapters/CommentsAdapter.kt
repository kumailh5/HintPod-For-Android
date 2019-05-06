package com.kumail.hintpod.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumail.hintpod.R
import com.kumail.hintpod.data.Comment
import kotlinx.android.synthetic.main.hintpod_item_comment.view.*

class CommentsAdapter(val commentsList: List<Comment>, val context: Context) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return commentsList.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.hintpod_item_comment, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.commentTextView.text = commentsList[position].content
//        holder.tvSuggestionBody?.text = suggestionList.get(position).content

//        Picasso
//                .get() // give it the context
//                .load("https://avatars2.githubusercontent.com/u/1442761?s=88&v=4") // load the image
//                .into(holder.ivStatusIndicator) // select the ImageView to load it into

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add information to
        val commentTextView = view.tv_comment!!
    }

}


