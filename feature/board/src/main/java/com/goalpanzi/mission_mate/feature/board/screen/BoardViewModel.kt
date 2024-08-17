package com.goalpanzi.mission_mate.feature.board.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goalpanzi.mission_mate.core.domain.usecase.DeleteMissionUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetMissionBoardsUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetMissionUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetMissionVerificationsUseCase
import com.goalpanzi.mission_mate.feature.board.model.MissionState
import com.goalpanzi.mission_mate.feature.board.model.MissionState.Companion.getMissionState
import com.goalpanzi.mission_mate.feature.board.model.uimodel.MissionBoardUiModel
import com.goalpanzi.mission_mate.feature.board.model.uimodel.MissionUiModel
import com.goalpanzi.mission_mate.feature.board.model.uimodel.MissionVerificationUiModel
import com.luckyoct.core.model.base.NetworkResult
import com.luckyoct.core.model.response.BoardReward
import com.luckyoct.core.model.response.MissionBoardsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val getMissionUseCase: GetMissionUseCase,
    private val getMissionBoardsUseCase: GetMissionBoardsUseCase,
    private val getMissionVerificationsUseCase: GetMissionVerificationsUseCase,
    private val deleteMissionUseCase: DeleteMissionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val missionId: Long = savedStateHandle.get<Long>("missionId")!!

    private val _deleteMissionResultEvent = MutableSharedFlow<Boolean>()
    val deleteMissionResultEvent: SharedFlow<Boolean> = _deleteMissionResultEvent.asSharedFlow()

    private val _missionBoardUiModel =
        MutableStateFlow<MissionBoardUiModel>(MissionBoardUiModel.Loading)
    val missionBoardUiModel: StateFlow<MissionBoardUiModel> = _missionBoardUiModel.asStateFlow()

    private val _missionUiModel =
        MutableStateFlow<MissionUiModel>(MissionUiModel.Loading)
    val missionUiModel: StateFlow<MissionUiModel> = _missionUiModel.asStateFlow()

    private val _missionVerificationUiModel =
        MutableStateFlow<MissionVerificationUiModel>(MissionVerificationUiModel.Loading)
    val missionVerificationUiModel: StateFlow<MissionVerificationUiModel> =
        _missionVerificationUiModel.asStateFlow()

    val missionState: StateFlow<MissionState> =
        combine(
            missionBoardUiModel.filter { it is MissionBoardUiModel.Success },
            missionUiModel.filter { it is MissionUiModel.Success },
            missionVerificationUiModel.filter { it is MissionVerificationUiModel.Success }
        ) { missionBoard, mission, missionVerification ->
            getMissionState(missionBoard, mission, missionVerification)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = MissionState.LOADING
        )

    val boardRewardEvent: SharedFlow<BoardReward?> = missionBoardUiModel.map {
        when (it) {
            is MissionBoardUiModel.Success -> {
                it.missionBoardsResponse.missionBoards.find { result ->
                    result.isMyPosition && result.number != 0 && result.number != it.missionBoardsResponse.missionBoards.size
                }?.reward
            }

            else -> {
                null
            }
        }
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500)
    )

    fun getMissionBoards() {

        viewModelScope.launch {
            _missionBoardUiModel.emit(MissionBoardUiModel.Loading)

            getMissionBoardsUseCase(missionId)
                .catch {
                    _missionBoardUiModel.emit(MissionBoardUiModel.Error)
                }.collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            _missionBoardUiModel.emit(
                                MissionBoardUiModel.Success(
                                    it.data
                                )
                            )
                        }

                        else -> {
                            _missionBoardUiModel.emit(MissionBoardUiModel.Error)
                        }
                    }
                }
        }
    }

    fun getMission() {
        viewModelScope.launch {
            getMissionUseCase(missionId).catch {
                _missionUiModel.emit(MissionUiModel.Error)
            }.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _missionUiModel.emit(MissionUiModel.Success(it.data))
                    }

                    else -> {
                        _missionUiModel.emit(MissionUiModel.Error)
                    }
                }
            }
        }
    }

    fun getMissionVerification() {
        viewModelScope.launch {
            getMissionVerificationsUseCase(missionId).catch {
                _missionVerificationUiModel.emit(MissionVerificationUiModel.Error)
            }.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _missionVerificationUiModel.emit(MissionVerificationUiModel.Success(it.data))
                    }

                    else -> {
                        _missionVerificationUiModel.emit(MissionVerificationUiModel.Error)
                    }
                }
            }
        }
    }

    fun deleteMission() {
        viewModelScope.launch {
            deleteMissionUseCase(missionId)
                .catch {
                    _deleteMissionResultEvent.emit(false)
                }.collect {
                    _deleteMissionResultEvent.emit(true)
                }
        }
    }

    fun verify() {

    }
}