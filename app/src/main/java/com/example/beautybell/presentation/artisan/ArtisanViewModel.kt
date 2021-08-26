package com.example.beautybell.presentation.artisan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautybell.domain.artisan.entity.ArtisanEntity
import com.example.beautybell.domain.artisan.usecase.GetAllArtisanUseCase
import com.example.beautybell.domain.common.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtisanViewModel @Inject constructor(private val getAllArtisanUseCase: GetAllArtisanUseCase) :
    ViewModel(){

    private val state = MutableStateFlow<ArtisanFragmenState>(ArtisanFragmenState.Init)
    val mState: StateFlow<ArtisanFragmenState> get() = state

    private var artisans = MutableStateFlow<List<ArtisanEntity>>(mutableListOf())
    var mArtisans: StateFlow<List<ArtisanEntity>> get() = artisans
        set(value) {}

    private fun setLoading(){
        state.value = ArtisanFragmenState.IsLoading(true)
    }

    private fun hideLoading(){
        state.value = ArtisanFragmenState.IsLoading(false)
    }

    private fun showToast(message: String){
        state.value = ArtisanFragmenState.ShowToast(message)
    }

    init {
        fetchAllArtisan()
    }

    fun fetchAllArtisan() {
        viewModelScope.launch {
            getAllArtisanUseCase.invoke()
                .onStart {
                    setLoading()
                }.catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }.collect { result ->
                    hideLoading()
                    when(result) {
                        is BaseResult.Success -> {
                            artisans.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.error)
                        }
                    }
                }
        }
    }
}

sealed class ArtisanFragmenState {
    object Init : ArtisanFragmenState()
    data class IsLoading(val isLoading: Boolean) : ArtisanFragmenState()
    data class ShowToast(val message: String) : ArtisanFragmenState()
}