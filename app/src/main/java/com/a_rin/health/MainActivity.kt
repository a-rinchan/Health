package com.a_rin.health

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var realm : Realm ?= null
    lateinit var calendar : Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        val realmResults = realm?.where(ItemData::class.java)?.findAll()

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = realmResults?.let { RecyclerViewAdapter(this, it, autoUpdate = true) }

        calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        //新規記録 ダイアログ
        inputButton.setOnClickListener {

            Log.d("月日", year.toString() + month.toString() + day.toString())

            var dialog  : DatePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    // Display Selected date in textbox

                    val intent = Intent(this, InputActivity::class.java)
                    intent.putExtra("DAY_KEY", mDay)
                    //mMonthのみ0から始まっているため+1
                    intent.putExtra("MONTH_KEY", mMonth + 1)
                    intent.putExtra("YEAR_KEY", mYear)
                    startActivity(intent)
                }, year, month, day
            )
            dialog.show()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }

}
