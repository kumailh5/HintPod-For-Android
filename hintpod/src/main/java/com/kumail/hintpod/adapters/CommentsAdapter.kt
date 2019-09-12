package com.kumail.hintpod.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.kumail.hintpod.R
import com.kumail.hintpod.data.Comment
import kotlinx.android.synthetic.main.hintpod_item_comment.view.*
import kotlin.random.Random

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
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.commentTextView.text = commentsList[position].content
//        holder.pillView.setBackgroundColor(R.color.hintpod_colorPrimary)
//        holder.pillView.background.setColorFilter( PorterDuffColorFilter(0xFF4FC3F, PorterDuff.Mode.MULTIPLY))

        val colors: IntArray = intArrayOf(R.color.hintpod_colorAccent,
                R.color.hintpod_colorPrimary,
                R.color.hintpod_colorPrimaryDark,
                R.color.hintpod_green,
                R.color.hintpod_red,
                R.color.hintpod_yellow,
                R.color.hintpod_orange,
                R.color.hintpod_red_dark,
                R.color.hintpod_pink,
                R.color.hintpod_indigo,
                R.color.hintpod_purple,
                R.color.hintpod_teal)
        val col =  colors.get( Random.nextInt(colors.size))
        holder.pillView.background.setTint(getColor(context, col))

//        holder.nameTextView.text =
//        holder.tvSuggestionBody?.text = suggestionList.get(position).content

//        Picasso
//                .get() // give it the context
//                .load("https://avatars2.githubusercontent.com/u/1442761?s=88&v=4") // load the image
//                .into(holder.ivStatusIndicator) // select the ImageView to load it into

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add information to
        val nameTextView = view.tv_name!!
        val commentTextView = view.tv_comment!!
        val pillView = view.v_pill!!
    }

}


