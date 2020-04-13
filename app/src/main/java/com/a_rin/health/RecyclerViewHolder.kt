package com.a_rin.health

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
 * タイトル : RecyclerViewHolder
 * 説明 : メイン画面用のViewHolder
 *
 * @author Ayaka Yoshizawa
 */

class RecyclerViewHolder(var view : View) : RecyclerView.ViewHolder(view) {

    val container = view.findViewById<LinearLayout>(R.id.container)
    var faceImageView = view.findViewById<ImageView>(R.id.faceImageView)
    var dateTextView = view.findViewById<TextView>(R.id.dateTextView)
    var messageTextView = view.findViewById<TextView>(R.id.messageTextView)
}