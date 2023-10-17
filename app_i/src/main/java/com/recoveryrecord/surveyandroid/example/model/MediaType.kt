package com.recoveryrecord.surveyandroid.example.model

import com.recoveryrecord.surveyandroid.example.config.Constants.CHINATIMES_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.CHINATIMES_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.CNA_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.CNA_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.CTS_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.CTS_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.EBC_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.EBC_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.ETTODAY_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.ETTODAY_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.LTN_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.LTN_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.SETN_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.SETN_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.STORM_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.STORM_ENG
import com.recoveryrecord.surveyandroid.example.config.Constants.UDN_CHI
import com.recoveryrecord.surveyandroid.example.config.Constants.UDN_ENG

sealed class MediaType(
    val englishId: String,
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
        const val DEFAULT_MEDIA_ORDER =
            "$CHINATIMES_CHI 1, $CNA_CHI 2, $EBC_CHI 3, $CTS_CHI 4, $LTN_CHI 5, $STORM_CHI 6, $UDN_CHI 7, $ETTODAY_CHI 8, $SETN_CHI 9"

        //        "自由時報 5, 華視 3, ettoday 8, 風傳媒 6, 中央社 2, 聯合 7, 三立 9, 東森 4, 中時 1"
        private val mediaMap: Map<String, String> = mapOf(
            CNA_ENG to CNA_CHI,
            CHINATIMES_ENG to CHINATIMES_CHI,
            CTS_ENG to CTS_CHI,
            EBC_ENG to EBC_CHI,
            LTN_ENG to LTN_CHI,
            STORM_ENG to STORM_CHI,
            UDN_ENG to UDN_CHI,
            ETTODAY_ENG to ETTODAY_CHI,
            SETN_ENG to SETN_CHI
        )

        val mediasIDMap = mapOf(
            CHINATIMES_ENG to "1",
            CNA_ENG to "2",
            CTS_ENG to "3",
            LTN_ENG to "4",
            STORM_ENG to "5",
            UDN_ENG to "6",
            ETTODAY_ENG to "7",
            SETN_ENG to "8",
            EBC_ENG to "9"
        )

        private val mediaListEnglish = lazy { mediaMap.keys.toList() }
        private val mediaListChinese = lazy { mediaMap.values.toList() }
        fun getAllMedia(english: Boolean = true): List<String> {
            return if (english) mediaListEnglish.value
            else mediaListChinese.value
        }

        @JvmStatic
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


    object Storm : MediaType(STORM_ENG, STORM_CHI)
    object Setn : MediaType(SETN_ENG, SETN_CHI)
    object Ltn : MediaType(LTN_ENG, LTN_CHI)
    object Ettoday : MediaType(ETTODAY_ENG, ETTODAY_CHI)
    object Ebc : MediaType(EBC_ENG, EBC_CHI)
    object Cts : MediaType(CTS_ENG, CTS_CHI)
    object Cna : MediaType(CNA_ENG, CNA_CHI)
    object Chinatimes : MediaType(CHINATIMES_ENG, CHINATIMES_CHI)
    object Udn : MediaType(UDN_ENG, UDN_CHI)
}