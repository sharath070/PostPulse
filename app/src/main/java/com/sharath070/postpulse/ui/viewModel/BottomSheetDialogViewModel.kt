package com.sharath070.postpulse.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BottomSheetDialogViewModel: ViewModel() {

    private var _selectedFilterForHotPosts = MutableLiveData<String>("Viral")
    val selectedFilterForHotPosts: LiveData<String> get() = _selectedFilterForHotPosts

    fun hotPostsViralSelected(){
        _selectedFilterForHotPosts.value = "viral"
    }

    fun hotPostsRaisingSelected(){
        _selectedFilterForHotPosts.value = "Raising"
    }

    private var _selectedFilterForTopPosts = MutableLiveData<String>("Viral")
    val selectedFilterForTopPosts: LiveData<String> get() = _selectedFilterForTopPosts

    fun topPostsViralSelected(){
        _selectedFilterForTopPosts.value = "viral"
    }

    fun topPostsRaisingSelected(){
        _selectedFilterForTopPosts.value = "Raising"
    }

}