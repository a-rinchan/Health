package com.a_rin.health

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class RecyclerViewAdapter(private val context: Context, private val objects: OrderedRealmCollection<ItemData>, private val autoUpdate: Boolean) : RealmRecyclerViewAdapter<ItemData, RecyclerViewHolder>(objects, autoUpdate) {

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
        holder.messageTextView.text = variationMessage(items.average)
    }

    fun variationMessage(average : Int? ): String {
        val messageNo0 = listOf<String>("気持ちに余裕を..!", "一日頑張ったね!", "一息ついてみよう!")
        val messageNo1 = listOf<String>("明日はきっと楽しいよ", "湯船に浸かってみよう", "ちょっと振り返ろう")
        val messageNo2 = listOf<String>("お疲れ様でした!", "よく頑張ったね!", "もっと良い日にしよう")
        val messageNo3 = listOf<String>("今日もいい日だったね", "明日も頑張ろう!", "休憩する時間も取ろう")
        val messageNo4 = listOf<String>("明日も最高な１日に!", "その調子で行こう!", "休む時間も大切にね!")

        var random  =  (0 .. 2).random()

        when(average){
            0 -> return messageNo0[random]
            1 -> return messageNo1[random]
            2 -> return messageNo2[random]
            3 -> return messageNo3[random]
            4 -> return messageNo4[random]
            else -> return "お疲れ様でした!"
        }
    }

}