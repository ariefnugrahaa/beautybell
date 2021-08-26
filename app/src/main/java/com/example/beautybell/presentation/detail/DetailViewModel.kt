package com.example.beautybell.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautybell.domain.common.base.BaseResult
import com.example.beautybell.domain.detail.entity.DetailEntity
import com.example.beautybell.domain.detail.usecase.GetArtisanByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(private val getArtisanByIdUseCase: GetArtisanByIdUseCase):
    ViewModel()
{

    private val _state = MutableStateFlow<DetailFragmentState>(DetailFragmentState.Init)
    val state : StateFlow<DetailFragmentState> get() = _state

    private val _detail = MutableStateFlow<DetailEntity?>(null)
    val detail : StateFlow<DetailEntity?> get() = _detail

    private fun setLoading(){
        _state.value = DetailFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = DetailFragmentState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = DetailFragmentState.ShowToast(message)
    }

    fun getArtisanById(id: String){
        viewModelScope.launch {
            getArtisanByIdUseCase.invoke(id)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.stackTraceToString())
                }
                .collect { result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success -> {
                            _detail.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.error)
                        }
                    }
                }
        }
    }
}

sealed class DetailFragmentState {
    object Init : DetailFragmentState()
    data class IsLoading(val isLoading: Boolean) : DetailFragmentState()
    data class ShowToast(val message : String) : DetailFragmentState()
}