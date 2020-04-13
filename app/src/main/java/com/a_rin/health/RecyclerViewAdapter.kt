package com.a_rin.health

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/*
 * タイトル : RecyclerViewAdapter
 * 説明 : メイン画面用のアダプター
 *
 * @author Ayaka Yoshizawa
 */

class RecyclerViewAdapter(
    private val context: Context,
    private val objects: OrderedRealmCollection<ItemData>,
    private var listener: OnItemClickListener,
    private val autoUpdate: Boolean) : RealmRecyclerViewAdapter<ItemData, RecyclerViewHolder>(objects, autoUpdate) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = inflater.inflate(R.layout.recycler_item, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = objects.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val items = objects.get(position)

        holder.faceImageView.setImageResource(
            when(items.average){
                0 -> R.drawable.no5_face
                1 -> R.drawable.no4_face
                2 -> R.drawable.no3_face
                3 -> R.drawable.no2_face
                4 -> R.drawable.no1_face
                else -> R.drawable.no3_face
            }
        )

        holder.dateTextView.text = "${items.year.toString()}/${items.month.toString()}/${items.day.toString()}"
        holder.messageTextView.text = items.message

        holder.container.setOnClickListener {
            listener.onItemClick(items)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(items: ItemData)
    }
}
