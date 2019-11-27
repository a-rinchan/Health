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
const val seekbarInitialValue = 1

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

        saveButton.setOnClickListener {
            saveRealm(inputYear, inputMonth, inputDay, luckValue, satietyValue, fitnessValue, sleepValue)
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }

    }

    fun SeekbarInitialization(seekbar : SeekBar) {
        //seekbar初期値
        seekbar.setProgress(0)
        //seekbar最大値
        seekbar.setMax(4)
    }

    //Realmで保存
    fun saveRealm(inputYear : Int?, inputMonth : Int?, inputDay : Int?,luckyValue : Int, satietyValue : Int, fitnessValue : Int, sleepValue : Int) {
        //画像を切り替えるためにaveraveの値を計算
        val average = (luckyValue + satietyValue + fitnessValue + sleepValue) / seekbarAmount

        realm?.executeTransaction{
            var itemData : ItemData = it.createObject(ItemData::class.java, UUID.randomUUID().toString())
            itemData.year = inputYear
            itemData.month = inputMonth
            itemData.day = inputDay
            itemData.lucky = luckyValue
            itemData.satiety = satietyValue
            itemData.fitness = fitnessValue
            itemData.sleep = sleepValue
            itemData.average = average
            it.copyToRealm(itemData)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }
}
