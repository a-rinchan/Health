package com.a_rin.health

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_input.*
import java.text.SimpleDateFormat
import java.util.*

/*
 * タイトル : InputActivity
 * 説明 : 気分の入力と保存を行う
 *
 * @author Ayaka Yoshizawa
 */

// SeekBarのボリュームサイズ
const val seekbarAmount = 4
// SeekBarのボリューム初期値
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

    /**
     * @Description SeekBarの初期化
     */

    fun SeekbarInitialization(seekbar : SeekBar) {
        //seekbar初期値
        seekbar.setProgress(0)
        //seekbar最大値
        seekbar.setMax(4)
    }

    /**
     * @Description Realmを使用し、データ保存を行う
     * @param date : 日付(キー), inputYear : 記録する年, inputMonth : 記録する月, inputDay : 記録する日時, luckyValue : 今日のいいことの値, satietyValue : 満腹度の値, fitnessValue : 運動の値, sleepValue : 睡眠の値
     */

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

    /**
     * @Description 記録の更新を行う
     * @param date : 記録する日時(キーとして利用), luckyValue : 今日のいいことの値, satietyValue : 満腹度の値, fitnessValue : 運動の値, sleepValue : 睡眠の値
     */

    fun updateRealm(date: String?, luckyValue : Int, satietyValue : Int, fitnessValue : Int, sleepValue : Int) {
        //気分度
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

    /**
     * @Description 気分度によって、メッセージをランダムに決定する
     * @param average : 気分度
     */

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
