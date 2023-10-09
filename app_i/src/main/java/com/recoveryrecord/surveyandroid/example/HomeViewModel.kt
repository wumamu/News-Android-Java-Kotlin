package com.recoveryrecord.surveyandroid.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.repository.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _newsFlow = MutableStateFlow<List<News>>(emptyList())
    val newsFlow = _newsFlow.asStateFlow()

    fun getNews(source: String, category: String, limit: Long) {
        viewModelScope.launch {
            _newsFlow.value = getNewsUseCase(source, category)
        }
    }
}