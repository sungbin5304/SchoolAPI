package me.sungbin.schoolapi.library.school

import android.os.StrictMode
import me.sungbin.schoolapi.library.school.meal.SchoolMeal
import me.sungbin.schoolapi.library.school.meal.SchoolMealParser
import me.sungbin.schoolapi.library.school.schedule.SchoolSchedule
import me.sungbin.schoolapi.library.school.schedule.SchoolScheduleParser
import me.sungbin.schoolapi.library.util.StringUtil
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.util.*

class School(
    private var type: SchoolType,
    private var region: SchoolRegion,
    private var code: String
) {
    companion object {
        private val MONTHLY_MENU_URL = "sts_sci_md00_001.do"
        private val SCHEDULE_URL = "sts_sci_sf01_001.do"
        private val SCHOOL_CODE_URL = "spr_ccm_cm01_100.do"

        @Throws(SchoolException::class)
        private fun getContentFromUrl(url: URL): String {
            try {
                StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder().permitNetwork().build()
                )

                val reader = BufferedReader(InputStreamReader(url.openStream()))
                val buffer = StringBuilder()

                while (true) {
                    val inputLine = reader.readLine() ?: break
                    buffer.append(inputLine)
                }

                reader.close()
                return buffer.toString()
            } catch (exception: Exception) {
                throw SchoolException("Fail to Connection.\n\n${exception.localizedMessage}")
            }

        }

        @Throws(SchoolException::class)
        fun find(region: SchoolRegion, name: String): School {
            try {
                val targetUrl = StringBuilder()

                targetUrl.append("https://par.").append(region.url).append("/").append(
                    SCHOOL_CODE_URL
                )
                targetUrl.append("?kraOrgNm=").append(URLEncoder.encode(name, "utf-8"))
                targetUrl.append("&")

                var content = getContentFromUrl(URL(targetUrl.toString()))
                content =
                    StringUtil.before(StringUtil.after(content, "orgCode"), "schulCrseScCodeNm")

                val schoolCode = content.substring(3, 13)
                val schoolType =
                    StringUtil.before(StringUtil.after(content, "schulCrseScCode\":\""), "\"")

                return School(
                    SchoolType.values()[Integer.parseInt(schoolType) - 1],
                    region,
                    schoolCode
                )

            } catch (exception: Exception) {
                throw SchoolException("Error at Find School.\n\n${exception.localizedMessage}")
            }

        }
    }

    private val monthlyMealCache = HashMap<Int, List<SchoolMeal>>()
    private val monthlyScheduleCache = HashMap<Int, List<SchoolSchedule>>()

    @Throws(SchoolException::class)
    fun getMonthlyMeal(year: Int, month: Int): List<SchoolMeal> {
        val cacheKey = year * 12 + month

        if (monthlyMealCache.containsKey(cacheKey)) return monthlyMealCache[cacheKey]!!

        val targetUrl = StringBuilder().run {
            append("https://stu.").append(region.url).append("/").append(MONTHLY_MENU_URL)
            append("?schulCode=").append(code)
            append("&schulCrseScCode=").append(type.id)
            append("&schulKndScCode=0").append(type.id)
            append("&schYm=").append(year).append(String.format("%02d", month))
            append("&")
        }

        try {
            var content = getContentFromUrl(URL(targetUrl.toString()))
            content = StringUtil.before(StringUtil.after(content, "<tbody>"), "</tbody>")

            val monthlyMenu = SchoolMealParser.parse(content)
            monthlyMealCache[cacheKey] = monthlyMenu

            return monthlyMenu
        } catch (exception: Exception) {
            throw SchoolException("Error at Connection.\n\n${exception.localizedMessage}")
        }

    }

    @Throws(SchoolException::class)
    fun getMonthlySchedule(year: Int, month: Int): List<SchoolSchedule> {
        val cacheKey = year * 12 + month

        if (monthlyScheduleCache.containsKey(cacheKey)) return monthlyScheduleCache[cacheKey]!!

        val targetUrl = StringBuilder().run {
            append("https://stu.").append(region.url).append("/").append(SCHEDULE_URL)
            append("?schulCode=").append(code)
            append("&schulCrseScCode=").append(type.id)
            append("&schulKndScCode=0").append(type.id)
            append("&ay=").append(year)
            append("&mm=").append(String.format("%02d", month))
            append("&")
        }

        try {
            var content = getContentFromUrl(URL(targetUrl.toString()))
            content = StringUtil.before(StringUtil.after(content, "<tbody>"), "</tbody>")

            val monthlySchedule = SchoolScheduleParser.parse(content)
            monthlyScheduleCache[cacheKey] = monthlySchedule

            return monthlySchedule
        } catch (exception: Exception) {
            throw SchoolException("Error at Connection.\n\n${exception.localizedMessage}")
        }

    }

    fun clearCache() {
        monthlyScheduleCache.clear()
        monthlyMealCache.clear()
    }
}