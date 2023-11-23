package com.example.froumapp.ui.forum.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.CategoryRepository
import com.example.froumapp.data.repository.PostRepository
import com.example.froumapp.data.responses.CategoryResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository
): BaseViewModel(repository) {
    private val _categoryResponse: MutableLiveData<Resource<CategoryResponse>> = MutableLiveData()
    val categoryResponse: LiveData<Resource<CategoryResponse>>
        get() = _categoryResponse

    fun getCategories() = viewModelScope.launch {
        _categoryResponse.value = repository.getCategories()
    }
}