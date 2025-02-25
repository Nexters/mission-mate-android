package com.goalpanzi.mission_mate.feature.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goalpanzi.mission_mate.core.domain.common.DomainResult
import com.goalpanzi.mission_mate.core.domain.history.usecase.GetMissionHistoriesUseCase
import com.goalpanzi.mission_mate.feature.history.model.History
import com.goalpanzi.mission_mate.feature.history.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMissionHistoriesUseCase: GetMissionHistoriesUseCase
) : ViewModel() {

    private var _page = 0

    private val _historyState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val historyState: StateFlow<HistoryUiState> = _historyState.asStateFlow()

    fun initHistories() {
        viewModelScope.launch {
            _historyState.emit(HistoryUiState.Loading)
            getHistories(0)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _historyState.emit(HistoryUiState.Refreshing)
            getHistories(0)
        }
    }

    fun fetchHistories() {
        viewModelScope.launch {
            getHistories(_page + 1)
        }
    }

    private suspend fun getHistories(page: Int) {
        _page = page
        getMissionHistoriesUseCase(page).collect { result ->
            val uiState = when (result) {
                is DomainResult.Success -> {
                    val data = result.data.toUiModel()

                    if(page == 0) HistoryUiState.Success(data)
                    else HistoryUiState.Success(
                        data.copy(resultList = getCurrentList(historyState.value) + data.resultList)
                    )
                }

                is DomainResult.Error -> {
                    HistoryUiState.Loading
                }

                is DomainResult.Exception -> {
                    HistoryUiState.Loading
                }
            }
            _historyState.emit(uiState)
        }
    }

    private fun getCurrentList(
        historyUiState: HistoryUiState
    ): List<History> {
        return if (historyUiState is HistoryUiState.Success) historyUiState.histories.resultList
        else emptyList()
    }
}
