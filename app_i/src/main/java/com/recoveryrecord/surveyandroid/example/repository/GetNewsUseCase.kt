package com.recoveryrecord.surveyandroid.example.repository

import com.recoveryrecord.surveyandroid.example.config.Constants
import com.recoveryrecord.surveyandroid.example.model.News
import javax.inject.Inject

class GetNewsUseCase
    @Inject
    constructor(
        private val repository: NewsRepository,
    ) {
        suspend operator fun invoke(
            source: String,
            category: String,
            pageSize: Long = Constants.NEWS_LIMIT_PER_PAGE,
        ): List<News> {
            return repository
                .getNews(source, category, pageSize)
                .fold(
                    onSuccess = { querySnapshot ->
                        querySnapshot.documents.mapNotNull { document ->
                            News(
                                title = document.getString("title"),
                                media = document.getString("media"),
                                id = document.getString("id"),
                                pubDate = document.getTimestamp("pubdate"),
                                image = document.getString("image"),
                            )
                        }
                    },
                    onFailure = {
                        // Handle the error here or log it
                        emptyList()
                    },
                )
        }
    }

sealed class NewsViewState {
    object Loading : NewsViewState()

    data class Success(val newsList: List<News>) : NewsViewState()

    data class Error(val error: Throwable) : NewsViewState()
}
