package com.recoveryrecord.surveyandroid.util

import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun updateRemote(document: DocumentReference, newSet: HashMap<String, Any>) {
    try {
        withContext(Dispatchers.IO) {
            document.update(newSet).await()
        }
    } catch (e: Exception) {
        Timber.d("Fire store uodate failed$e")

    }
}