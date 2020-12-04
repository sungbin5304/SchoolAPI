package me.sungbin.schoolapi.library.school.meal

sealed class SchoolMealType {
    object BREAKFAST : SchoolMealType()
    object LUNCH : SchoolMealType()
    object DINNER : SchoolMealType()
}
