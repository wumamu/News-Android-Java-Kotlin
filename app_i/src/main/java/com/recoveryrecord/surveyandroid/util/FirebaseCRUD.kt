package com.recoveryrecord.surveyandroid.util

import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun updateRemote(
    document: DocumentReference,
    newData: HashMap<String, Any>,
    onSuccess: () -> Unit = {},
) {
    try {
        withContext(Dispatchers.IO) {
            document.update(newData).await()
            onSuccess()
        }
    } catch (e: Exception) {
        Timber.w("Fire store uodate failed$e")
    }
}

suspend fun insertRemote(
    document: DocumentReference,
    newData: HashMap<String, Any>,
    onSuccess: () -> Unit = {},
//    onError: (Exception) -> Unit
) {
    try {
        withContext(Dispatchers.IO) {
            //TODO remove
            Timber.d("1")
            document.set(newData).await().apply { Timber.d("2") }
            Timber.d("3")
            onSuccess()
        }
    } catch (e: Exception) {
        Timber.w("Fire store update failed$e")
    }
}