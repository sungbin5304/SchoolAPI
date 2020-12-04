package me.sungbin.schoolapi.library.school.meal

class SchoolMeal {
    var breakfast: String? = null // 아침
    var lunch: String? = null // 점심
    var dinner: String? = null // 저녁

    override fun toString() = "$breakfast\n$lunch\n$dinner"
}