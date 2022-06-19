package com.venator.venatorstoreapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.venator.venatorstoreapp.Comment
import com.venator.venatorstoreapp.R
import java.util.*

class CommentsAdapter(private val inflater: LayoutInflater):ListAdapter<Comment, CommentsAdapter.ViewHolder>(CommentDiffCallback) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.commentTitle)
        private val text = view.findViewById<TextView>(R.id.commentText)
        private var comment: Comment? = null

        fun bind(comment: Comment) {
            this.comment = comment
            title.text =
                comment.authorId.toString() + " – " + comment.dateAdded.get(Calendar.DAY_OF_MONTH)
                    .toString() + "." + comment.dateAdded.get(Calendar.MONTH)
                    .toString() + "." + comment.dateAdded.get(Calendar.YEAR).toString()
            text.text = comment.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    object CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldComment: Comment, newComment: Comment): Boolean =
            oldComment == newComment

        override fun areContentsTheSame(oldComment: Comment, newComment: Comment): Boolean {
            return oldComment.id == newComment.id &&
                    oldComment.authorId == newComment.authorId &&
                    oldComment.text == newComment.text &&
                    oldComment.dateAdded == newComment.dateAdded
        }
    }
}