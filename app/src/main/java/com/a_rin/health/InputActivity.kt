package com.a_rin.health

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_input.*
import java.text.SimpleDateFormat
import java.util.*

const val seekbarAmount = 4
const val seekbarInitialValue = 0

class InputActivity : AppCompatActivity() {

    private var realm : Realm ?= null

    var luckValue : Int = seekbarInitialValue
    var satietyValue : Int = seekbarInitialValue
    var fitnessValue : Int = seekbarInitialValue
    var sleepValue : Int = seekbarInitialValue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        realm = Realm.getDefaultInstance()

        val intent = getIntent()
        val inputDay = intent.extras?.getInt("DAY_KEY")
        val inputMonth = intent.extras?.getInt("MONTH_KEY")
        val inputYear = intent.extras?.getInt("YEAR_KEY")

        val date = "${inputYear.toString()}${inputMonth.toString()}${inputDay.toString()}"

        getDateText.text = "$inputYear/$inputMonth/$inputDay"

        SeekbarInitialization(LuckySeekbar)
        SeekbarInitialization(SatietySeekbar)
        SeekbarInitialization(FitnessSeekbar)
        SeekbarInitialization(SleepSeekbar)


        LuckySeekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    //Realm保存時に使う
                    luckValue = progress
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            }
        )

        SatietySeekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    //Realm保存時に使う
                    satietyValue = progress
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }
                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            }
        )

        FitnessSeekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    //Realm保存時に使う
                    fitnessValue = progress
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }
                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            }
        )

        SleepSeekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    //Realm保存時に使う
                    sleepValue = progress
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }
                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            }
        )

        //保存ボタンタップ時
        saveButton.setOnClickListener {
            val result = realm?.where(ItemData::class.java)?.equalTo("date", date)?.findFirst()

            if(result != null){
                updateRealm(date, luckValue, satietyValue, fitnessValue, sleepValue)
            }else {
                saveRealm(date, inputYear, inputMonth, inputDay, luckValue, satietyValue, fitnessValue, sleepValue)
            }
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //キャンセルボタンタップ時
        cancelButton.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    fun SeekbarInitialization(seekbar : SeekBar) {
        //seekbar初期値
        seekbar.setProgress(0)
        //seekbar最大値
        seekbar.setMax(4)
    }

    //Realmで保存
    fun saveRealm(date : String?, inputYear : Int?, inputMonth : Int?, inputDay : Int?,luckyValue : Int, satietyValue : Int, fitnessValue : Int, sleepValue : Int) {
        //画像を切り替えるためにaveraveの値を計算
        val average = (luckyValue + satietyValue + fitnessValue + sleepValue) / seekbarAmount

        realm?.executeTransaction{
            var itemData : ItemData = it.createObject(ItemData::class.java, date)
            itemData.year = inputYear
            itemData.month = inputMonth
            itemData.day = inputDay
            itemData.lucky = luckyValue
            itemData.satiety = satietyValue
            itemData.fitness = fitnessValue
            itemData.sleep = sleepValue
            itemData.average = average
            itemData.message = variationMessage(average)
            it.copyToRealm(itemData)
        }
    }

    //更新時
    fun updateRealm(date: String?, luckyValue : Int, satietyValue : Int, fitnessValue : Int, sleepValue : Int) {
        val average = (luckyValue + satietyValue + fitnessValue + sleepValue) / seekbarAmount

        realm?.executeTransaction{
            var itemData = realm?.where(ItemData::class.java)?.equalTo("date", date)?.findFirst()
                ?: return@executeTransaction
            itemData.lucky = luckyValue
            itemData.satiety = satietyValue
            itemData.fitness = fitnessValue
            itemData.sleep = sleepValue
            itemData.average = average
            itemData.message = variationMessage(average)
            it.copyToRealm(itemData)
        }
    }

    fun variationMessage(average : Int? ): String {
        val messageNo0 = listOf<String>(getString(R.string.variation_message_no0_0), getString(R.string.variation_message_no0_1), getString(
                    R.string.variation_message_no0_2))
        val messageNo1 = listOf<String>(getString(R.string.variation_message_no1_0), getString(R.string.variation_message_no1_1), getString(
                    R.string.variation_message_no1_2))
        val messageNo2 = listOf<String>(getString(R.string.variation_message_no2_0), getString(R.string.variation_message_no2_1), getString(
                    R.string.variation_message_no2_2))
        val messageNo3 = listOf<String>(getString(R.string.variation_message_no3_0), getString(R.string.variation_message_no3_1), getString(
                    R.string.variation_message_no3_2))
        val messageNo4 = listOf<String>(getString(R.string.variation_message_no4_0), getString(R.string.variation_message_no4_1), getString(
                    R.string.variation_message_no4_2))

        var random  =  (0 .. 2).random()

        when(average){
            0 -> return messageNo0[random]
            1 -> return messageNo1[random]
            2 -> return messageNo2[random]
            3 -> return messageNo3[random]
            4 -> return messageNo4[random]
            else -> return getString(R.string.variation_message_other)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }
}
