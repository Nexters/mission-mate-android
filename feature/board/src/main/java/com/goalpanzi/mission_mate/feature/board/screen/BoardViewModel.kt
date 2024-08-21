package com.goalpanzi.mission_mate.feature.board.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goalpanzi.core.model.base.NetworkResult
import com.goalpanzi.core.model.response.BoardReward
import com.goalpanzi.mission_mate.core.domain.usecase.DeleteMissionUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetCachedMemberIdUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetMissionBoardsUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetMissionUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetMissionVerificationsUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetMyMissionVerificationUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.GetViewedTooltipUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.ProfileUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.SetViewedTooltipUseCase
import com.goalpanzi.mission_mate.core.domain.usecase.VerifyMissionUseCase
import com.goalpanzi.mission_mate.feature.board.model.BoardPiece
import com.goalpanzi.mission_mate.feature.board.model.BoardPieceType
import com.goalpanzi.mission_mate.feature.board.model.MissionState
import com.goalpanzi.mission_mate.feature.board.model.MissionState.Companion.getMissionState
import com.goalpanzi.mission_mate.feature.board.model.UserStory
import com.goalpanzi.mission_mate.feature.board.model.toBoardPieces
import com.goalpanzi.mission_mate.feature.board.model.toCharacter
import com.goalpanzi.mission_mate.feature.board.model.toModel
import com.goalpanzi.mission_mate.feature.board.model.uimodel.MissionBoardUiModel
import com.goalpanzi.mission_mate.feature.board.model.uimodel.MissionUiModel
import com.goalpanzi.mission_mate.feature.board.model.uimodel.MissionVerificationUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCachedMemberIdUseCase: GetCachedMemberIdUseCase,
    getViewedTooltipUseCase: GetViewedTooltipUseCase,
    private val getMissionUseCase: GetMissionUseCase,
    private val getMissionBoardsUseCase: GetMissionBoardsUseCase,
    private val getMissionVerificationsUseCase: GetMissionVerificationsUseCase,
    private val deleteMissionUseCase: DeleteMissionUseCase,
    private val profileUseCase: ProfileUseCase,
    private val setViewedTooltipUseCase: SetViewedTooltipUseCase,
    private val verifyMissionUseCase: VerifyMissionUseCase,
    private val getMyMissionVerificationUseCase : GetMyMissionVerificationUseCase,
) : ViewModel() {

    val missionId: Long = savedStateHandle.get<Long>("missionId")!!

    val viewedToolTip: StateFlow<Boolean> = getViewedTooltipUseCase().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = true
    )

    private val _myMissionVerification = MutableSharedFlow<UserStory>()
    val myMissionVerification : SharedFlow<UserStory> = _myMissionVerification.asSharedFlow()

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

    val isHost: StateFlow<Boolean> =
        combine(
            getCachedMemberIdUseCase(),
            missionUiModel.filter { it is MissionUiModel.Success }
        ) { memberId, mission ->
            if (mission !is MissionUiModel.Success) return@combine false
            memberId == mission.missionDetail.hostMemberId
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = false
        )

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
    private val _boardRewardEvent = MutableSharedFlow<BoardReward?>()
    val boardRewardEvent: SharedFlow<BoardReward?> = _boardRewardEvent.asSharedFlow()

    private val _boardPieces = MutableStateFlow<List<BoardPiece>>(emptyList())
    val boardPieces: StateFlow<List<BoardPiece>> = _boardPieces.asStateFlow()

    fun getMissionBoards() {
        viewModelScope.launch {
            getMissionBoardsUseCase(missionId)
                .catch {
                    _missionBoardUiModel.emit(MissionBoardUiModel.Error)
                }.collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            _missionBoardUiModel.emit(
                                MissionBoardUiModel.Success(
                                    it.data.toModel()
                                )
                            )
                            _boardPieces.emit(
                                it.data.toModel().toBoardPieces(
                                    profileUseCase.getProfile()
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
                        _missionUiModel.emit(MissionUiModel.Success(it.data.toModel()))
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

    fun setViewedTooltip() {
        viewModelScope.launch {
            setViewedTooltipUseCase().collect()
        }
    }

    fun onVerifySuccess() {
        if(missionState.value != MissionState.IN_PROGRESS_MISSION_DAY_BEFORE_CONFIRM) return
        viewModelScope.launch {
            // 내 캐릭터
            val myBoardPiece = boardPieces.value.find { it.isMe }
            val missionBoard = missionBoardUiModel.value
            if (myBoardPiece != null && missionBoard is MissionBoardUiModel.Success) {
                val missionBoardList = missionBoard.missionBoards.missionBoardList
                // 내 캐릭터보다 한칸 앞션 캐릭터
                val nextBoardPiece = missionBoardList.filter { block ->
                    block.missionBoardMembers.isNotEmpty()
                }.find {
                    it.number == myBoardPiece.index + 1
                }

                // 내 캐릭터와 같이 있던 캐릭터
                val prevBoardPiece = missionBoardList.filter { block ->
                    block.missionBoardMembers.size >= 2
                }.find {
                    it.number == myBoardPiece.index
                }
                _boardPieces.emit(
                    buildList {
                        addAll(
                            boardPieces.value.map {
                                if (it.isMe) it.copy(
                                    boardPieceType = BoardPieceType.MOVED,
                                    count = if (nextBoardPiece != null) nextBoardPiece.missionBoardMembers.size + 1 else 1
                                ) else if (nextBoardPiece != null && it.index == nextBoardPiece.number) {
                                    it.copy(boardPieceType = BoardPieceType.HIDDEN)
                                } else it
                            }
                        )
                        if (prevBoardPiece != null) {
                            val target = prevBoardPiece.missionBoardMembers.first {
                                it.nickname != myBoardPiece.nickname
                            }
                            add(
                                BoardPiece(
                                    count = prevBoardPiece.missionBoardMembers.size - 1,
                                    nickname = target.nickname,
                                    drawableRes = target.character.imageId,
                                    index = prevBoardPiece.number,
                                    isMe = false
                                )
                            )
                        }
                    }
                )

                _missionBoardUiModel.emit(
                    missionBoard.copy(
                        missionBoards = missionBoard.missionBoards.copy(
                            passedCountByMe = missionBoard.missionBoards.passedCountByMe + 1
                        )
                    )
                )
                delay(400)
                _boardRewardEvent.emit(
                    missionBoardList.find {
                        myBoardPiece.index + 1 == it.number
                    }?.boardReward
                )
            }
            getMissionBoards()
            getMission()
            getMissionVerification()
        }
    }

    fun getMyMissionVerification(
        number : Int
    ){
        viewModelScope.launch {
            getMyMissionVerificationUseCase(
                missionId,
                number
            ).catch {

            }.collect {
                when(it){
                    is NetworkResult.Success -> {
                        _myMissionVerification.emit(
                            UserStory(
                                characterType = it.data.characterType.toCharacter(),
                                imageUrl = it.data.imageUrl,
                                isVerified = true,
                                nickname = it.data.nickname,
                                verifiedAt = it.data.verifiedAt
                            )
                        )
                    }
                    else -> {

                    }
                }



            }

        }
    }

}