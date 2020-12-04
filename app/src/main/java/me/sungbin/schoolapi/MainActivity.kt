package me.sungbin.schoolapi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.sungbin.schoolapi.library.school.School
import me.sungbin.schoolapi.library.school.SchoolRegion

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textview = findViewById<TextView>(R.id.textview)
        val school = School.find(SchoolRegion.CHUNGNAM, "서령고등학교")
        val mealData = ArrayList<String>()
        val scheduleData = ArrayList<String>()

        for ((index, value) in school.getMonthlyMeal(2020, 11).withIndex()) {
            val date = "[11월 ${index}일]"
            var _value =
                value.toString().replace("\\d".toRegex(), "").replace(".", "").replace("()", "")
            if (value.dinner == null) _value = "급식이 없습니다."
            val data = "$date\n$_value"
            mealData.add(data)
        }

        for ((index, value) in school.getMonthlySchedule(2020, 11).withIndex()) {
            val date = "[11월 ${index}일]"
            var _value = value.toString()
            if (_value.isEmpty()) _value = "학사일정이 없습니다."
            val data = "$date\n$_value"
            scheduleData.add(data)
        }

        textview.text = scheduleData.joinToString("\n\n")
    }
}