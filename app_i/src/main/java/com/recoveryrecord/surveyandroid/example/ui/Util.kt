package com.recoveryrecord.surveyandroid.example.ui

object Util {
    val stormMap = lazy {
        mapOf(
            0 to "新新聞",
            1 to "華爾街",
            2 to "國內",
            3 to "政治",
            4 to "國際",
            5 to "財經",
            6 to "專欄",
            7 to "評論",
            8 to "調查",
            9 to "娛樂",
            10 to "運動",
            11 to "軍事",
            12 to "科技",
            13 to "職場",
            14 to "健康",
            15 to "兩性",
            16 to "旅遊",
            17 to "美食",
            18 to "影音",
            19 to "汽車",
            20 to "房地產",
            21 to "歷史",
            22 to "藝文",
            23 to "人物"
        )
    }
    val setnMap = lazy {
        mapOf(
            0 to "政治",
            1 to "娛樂",
            2 to "生活",
            3 to "健康",
            4 to "社會",
            5 to "運動",
            6 to "旅遊",
            7 to "國際",
            8 to "名家",
            9 to "汽車",
            10 to "房產",
            11 to "財經",
            12 to "新奇",
            13 to "寵物",
            14 to "寶神",
            15 to "日韓",
            16 to "時尚",
            17 to "電影",
            18 to "音樂"
        )
    }
    val ltnMap = lazy {
        mapOf(
            0 to "政治",
            1 to "社會",
            2 to "生活",
            3 to "國際",
            4 to "財經",
            5 to "體育",
            6 to "娛樂",
            7 to "3C",
            8 to "汽車",
            9 to "時尚",
            10 to "蒐奇",
            11 to "健康",
            12 to "玩咖"
        )
    }
    val ettodayMap = lazy {
        mapOf(
            0 to "社會",
            1 to "影劇",
            2 to "生活",
            3 to "國際",
            4 to "地方",
            5 to "大陸",
            6 to "軍武",
            7 to "政治",
            8 to "寵物動物",
            9 to "體育",
            10 to "健康",
            11 to "旅遊",
            12 to "新奇",
            13 to "財經",
            14 to "房產雲",
            15 to "ET車雲",
            16 to "遊戲",
            17 to "3C家電",
            18 to "法律"
        )
    }
    val ebcMap = lazy {
        mapOf(
            0 to "社會",
            1 to "娛樂",
            2 to "E星聞",
            3 to "新奇",
            4 to "暖聞",
            5 to "政治",
            6 to "國際",
            7 to "兩岸",
            8 to "生活",
            9 to "財經",
            10 to "星座",
            11 to "房產",
            12 to "體育",
            13 to "汽車",
            14 to "EBC森談",
            15 to "健康",
            16 to "旅遊"
        )
    }
    val cnaMap = lazy {
        mapOf(
            0 to "政治",
            1 to "國際",
            2 to "兩岸",
            3 to "產經",
            4 to "證券",
            5 to "科技",
            6 to "生活",
            7 to "社會",
            8 to "地方",
            9 to "文化",
            10 to "運動",
            11 to "娛樂"
        )
    }
    val chinatimesMap = lazy {
        mapOf(
            0 to "政治",
            1 to "生活",
            2 to "娛樂",
            3 to "財經",
            4 to "國際",
            5 to "兩岸",
            6 to "社會",
            7 to "軍事",
            8 to "科技",
            9 to "體育",
            10 to "健康",
            11 to "運勢",
            12 to "寶島"
        )
    }
    val ctsMap = lazy {
        mapOf(
            0 to "產業",
            1 to "氣象",
            2 to "社會",
            3 to "政治",
            4 to "娛樂",
            5 to "國際",
            6 to "生活",
            7 to "運動",
            8 to "財經",
            9 to "地方",
            10 to "藝文",
            11 to "綜合",
            12 to "校園"
        )
    }
    val udnMap = lazy {
        mapOf(
            0 to "要聞",
            1 to "運動",
            2 to "全球",
            3 to "社會",
            4 to "產經",
            5 to "股市",
            6 to "生活",
            7 to "文教",
            8 to "評論",
            9 to "地方",
            10 to "兩岸",
            11 to "數位",
            12 to "Oops",
            13 to "閱讀"
        )
    }

    val mediaMap = mapOf(
        "storm" to stormMap.value,
        "setn" to setnMap.value,
        "ltn" to ltnMap.value,
        "ettoday" to ettodayMap.value,
        "ebc" to ebcMap.value,
        "cts" to ctsMap.value,
        "cna" to cnaMap.value,
        "chinatimes" to chinatimesMap.value,
        "udn" to udnMap.value
    )
}

object NewsMedia {
    private val mediaMap: Map<String, String> = mapOf(
        "cna" to "中央社",
        "chinatimes" to "中時",
        "cts" to "華視",
        "ebc" to "東森",
        "ltn" to "自由時報",
        "storm" to "風傳媒",
        "udn" to "聯合",
        "ettoday" to "ettoday",
        "setn" to "三立"
    )

    fun getEnglish(input: String?): String {
        return mediaMap[input] ?: ""
    }

    fun getChinese(input: String?): String {
        return mediaMap.entries.find { it.value == input }?.key ?: ""
    }
}