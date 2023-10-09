package com.recoveryrecord.surveyandroid.example.ui

import com.recoveryrecord.surveyandroid.example.Constants.CHINATIMES_ENG
import com.recoveryrecord.surveyandroid.example.Constants.CNA_ENG
import com.recoveryrecord.surveyandroid.example.Constants.CTS_ENG
import com.recoveryrecord.surveyandroid.example.Constants.EBC_ENG
import com.recoveryrecord.surveyandroid.example.Constants.ETTODAY_ENG
import com.recoveryrecord.surveyandroid.example.Constants.LTN_ENG
import com.recoveryrecord.surveyandroid.example.Constants.SETN_ENG
import com.recoveryrecord.surveyandroid.example.Constants.STORM_ENG
import com.recoveryrecord.surveyandroid.example.Constants.UDN_ENG

sealed class MediaType(
    val englisgId: String,
    val chineseId: String
) {
    private var _categoryMap: Map<Int, String> = emptyMap()
    val categoryMap: Map<Int, String>
        get() = _categoryMap

    fun updateCategoryMap(updatedMap: Map<Int, String>): Boolean {
        _categoryMap = updatedMap
        return true
    }

    companion object {
        private val mediaMap: Map<String, String> = mapOf(
            CNA_ENG to "中央社",
            CHINATIMES_ENG to "中時",
            CTS_ENG to "華視",
            EBC_ENG to "東森",
            LTN_ENG to "自由時報",
            STORM_ENG to "風傳媒",
            UDN_ENG to "聯合",
            ETTODAY_ENG to "ettoday",
            SETN_ENG to "三立"
        )

        private val mediaListEnglish = lazy { mediaMap.keys.toList() }
        private val mediaListChinese = lazy { mediaMap.values.toList() }
        fun getAllMedia(english: Boolean = true): List<String> {
            return if (english) mediaListEnglish.value
            else mediaListChinese.value
        }

        fun getChinese(input: String?): String {
            return mediaMap[input] ?: ""
        }

        fun getEnglish(input: String?): String {
            return mediaMap.entries.find { it.value == input }?.key ?: ""
        }

        private fun getObject(id: String): MediaType? {
            return when (id) {
                STORM_ENG -> Storm
                SETN_ENG -> Setn
                LTN_ENG -> Ltn
                ETTODAY_ENG -> Ettoday
                EBC_ENG -> Ebc
                CTS_ENG -> Cts
                CNA_ENG -> Cna
                CHINATIMES_ENG -> Chinatimes
                UDN_ENG -> Udn
                else -> null
            }
        }

        fun getCategoryMapByEnglishId(id: String): Map<Int, String> {
            return getObject(id)?.categoryMap ?: emptyMap()
        }

        fun updateCategoryMapBy(id: String, newCategory: List<String>): Boolean {
            val updatedMap = mutableMapOf<Int, String>()
            newCategory.forEachIndexed { index, value ->
                updatedMap[index] = value
            }
            return getObject(id)?.updateCategoryMap(updatedMap) ?: false
        }
    }


    object Storm : MediaType(STORM_ENG, "風傳媒")
    object Setn : MediaType(SETN_ENG, "三立")
    object Ltn : MediaType(LTN_ENG, "自由時報")
    object Ettoday : MediaType(ETTODAY_ENG, "ettoday")
    object Ebc : MediaType(EBC_ENG, "東森")
    object Cts : MediaType(CTS_ENG, "華視")
    object Cna : MediaType(CNA_ENG, "中央社")
    object Chinatimes : MediaType(CHINATIMES_ENG, "中時")
    object Udn : MediaType(UDN_ENG, "聯合")
}