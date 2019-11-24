package com.a_rin.health

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewHolder(var view : View) : RecyclerView.ViewHolder(view) {

    var faceImageView = view.findViewById<ImageView>(R.id.faceImageView)
    var dateTextView = view.findViewById<TextView>(R.id.dateTextView)
    var messageTextView = view.findViewById<TextView>(R.id.messageTextView)
}