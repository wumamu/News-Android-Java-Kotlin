package com.recoveryrecord.surveyandroid.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface NewsRepository {
    suspend fun getNews(source: String, category: String): Task<QuerySnapshot>
}