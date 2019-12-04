package com.a_rin.health

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    //spinnerの初期値
    var spinnerYear : Int? = null
    var spinnerMonth : Int? = null

    private var realm : Realm ?= null
    lateinit var calendar : Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        realm = Realm.getDefaultInstance()
        var realmResults = realm?.where(ItemData::class.java)?.equalTo("year", year)?.equalTo("month", month + 1)?.findAll()
        realmResults = realmResults?.sort("day")

        realmResults?.let { recyclerView(it) }

        //新規記録 ダイアログ
        inputButton.setOnClickListener {

            var dialog  : DatePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    // Display Selected date in textbox

                    val intent = Intent(this, InputActivity::class.java)
                    intent.putExtra("DAY_KEY", mDay)
                    //mMonthのみ0から始まっているため+1
                    intent.putExtra("MONTH_KEY", mMonth + 1)
                    intent.putExtra("YEAR_KEY", mYear)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }, year, month, day
            )
            dialog.show()

        }

        //spinnerの設置
        spinner()

        //年選択のスピナー
        yearSpinner.setOnItemSelectedListener (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val spinner = parent
                spinnerYear = spinner?.selectedItem.toString().toInt()
            }

        })

        //月選択のスピナー
        monthSpinner.setOnItemSelectedListener (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val spinner = parent
                spinnerMonth = spinner?.selectedItem.toString().toInt()
            }
        })

        //検索ボタン押した時
        searchButton.setOnClickListener {
            realmResults = realm?.where(ItemData::class.java)?.equalTo("year", spinnerYear)?.equalTo("month", spinnerMonth)?.findAll()
            realmResults = realmResults?.sort("day")
            realmResults?.let { recyclerView(it) }
        }
    }

    fun spinner() {

        ArrayAdapter.createFromResource(this, R.array.year_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                yearSpinner.adapter = adapter
            }

        ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                monthSpinner.adapter = adapter
            }

    }

    //RecyclerViewの表示
    fun recyclerView(realmResults : RealmResults<ItemData>) {
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = realmResults?.let {
            RecyclerViewAdapter(this, it, object : RecyclerViewAdapter.OnItemClickListener{
                //RecyclerViewの項目タップ時
                override fun onItemClick(items: ItemData) {
                    items.date?.let { it -> deleateDialog(it) }
                }
            },autoUpdate = true) }
    }

    //RecyclerViewの項目タップ時のダイアログ
    fun deleateDialog(date : String) {
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setMessage("この記録を削除しますか?")
            .setPositiveButton("Yes", { dialog, which ->
                deleate(date)
            })
            .setNegativeButton("No", { dialog, which ->

            })
            .show()
    }

    //RecyclerViewのタップされたデータ消去
    fun deleate(date : String) {
        realm?.executeTransaction {
            val items = realm?.where(ItemData::class.java)?.equalTo("date", date)?.findFirst()
                ?: return@executeTransaction
            items.deleteFromRealm()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }

}

