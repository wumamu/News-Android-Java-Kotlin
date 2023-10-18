package com.recoveryrecord.surveyandroid.example

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recoveryrecord.surveyandroid.example.model.News
import com.recoveryrecord.surveyandroid.example.repository.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {
    val dataModalArrayList: MutableLiveData<List<News>> = MutableLiveData()

//    private val _newsFlow = MutableStateFlow<List<News>>(emptyList())
//    val newsFlow = _newsFlow.asStateFlow()

//    fun getNews(source: String, category: String, pageSize: Long = Constants.NEWS_LIMIT_PER_PAGE) {
////        viewModelScope.launch {
////            _newsFlow.value = getNewsUseCase(source, category)
////        }
//        viewModelScope.launch {
//            // Perform time-consuming task using Dispatchers.IO
//            val news = withContext(Dispatchers.IO) {
//                getNewsUseCase(source, category, pageSize)
//            }
//            dataModalArrayList.postValue(news)
//        }
//    }
}