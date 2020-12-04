<img align="right" src="https://raw.githubusercontent.com/sungbin5304/SchoolAPI/master/school.png" width="12%" height="12%"/>

# SchoolAPI [![](https://jitpack.io/v/sungbin5304/SchoolAPI.svg)](https://jitpack.io/#sungbin5304/SchoolAPI)
학교 급식표 & 학사일정 조회 라이브러리

-----

# 다운로드
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.sungbin5304:SchoolAPI:{version}'
}
```

# 사용법
## 1. `School` 인스턴스 생성
```kotlin
School.find(region: SchoolRegion, name: String): School
```

## 지원하는 `SchoolRegion` 목록
1. `SEOUL`
2. `INCHEON`
3. `BUSAN`
4. `GWANGJU`
5. `DAEJEON`
6. `DAEGU`
7. `SEJONG`
8. `ULSAN`
9. `GYEONGGI`
10. `KANGWON`
11. `CHUNGBUK`
12. `CHUNGNAM`
13. `GYEONGBUK`
14. `GYEONGNAM`
15. `JEONBUK`
16. `JEONNAM`
17. `JEJU`

## 예시
```kotlin
val school = School.find(SchoolRegion.CHUNGNAM, "서령고등학교")
```

# 2. 데이터 조회 및 사용
## ㄱ. 학교 식단표 조회
```kotlin
School.getMonthlyMeal(year: Int, month: Int): List<SchoolMeal>
```

### 예시
```kotlin
for ((index, value) in school.getMonthlyMeal(2020, 11).withIndex()) {
    val date = "[11월 ${index}일]"
    var _value = value.toString().replace("\\d".toRegex(), "").replace(".", "").replace("()", "")
    if (value.dinner == null) _value = "급식이 없습니다."
    val data = "$date\n$_value"
    mealData.add(data)
}
```

## ㄴ. 학교 학사일정 조회
```kotlin
School.getMonthlySchedule(year: Int, month: Int): List<SchoolSchedule>
```

### 예시
```kotlin
for ((index, value) in school.getMonthlySchedule(2020, 11).withIndex()) {
    val date = "[11월 ${index}일]"
    var _value = value.toString()
    if (_value.isEmpty()) _value = "학사일정이 없습니다."
    val data = "$date\n$_value"
    scheduleData.add(data)
}
```

# Happy Coding :)
