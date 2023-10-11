package com.recoveryrecord.surveyandroid.repository

import com.recoveryrecord.surveyandroid.example.Constants
import com.recoveryrecord.surveyandroid.example.model.News
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(
        source: String,
        category: String,
        pageSize: Long = Constants.NEWS_LIMIT_PER_PAGE,
    ): List<News> {
        val querySnapshot = repository.getNews(source, category)
        val newsList = mutableListOf<News>()
        for (d in querySnapshot.result.documents) {
//                val news = document.toObject(News::class.java)
            News(
                title = d.getString("title"),
                media = d.getString("media"),
                id = d.getString("id"),
                pubDate = d.getTimestamp("pubdate"),
                image = d.getString("image")
            ).apply { newsList.add(this) }
        }
        return newsList
    }
}

sealed class NewsViewState {
    object Loading : NewsViewState()
    data class Success(val newsList: List<News>) : NewsViewState()
    data class Error(val error: Throwable) : NewsViewState()
}
