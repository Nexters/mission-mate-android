package com.goalpanzi.mission_mate.feature.history

import com.goalpanzi.mission_mate.feature.history.model.Histories

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Success(val histories: Histories): HistoryUiState
}
