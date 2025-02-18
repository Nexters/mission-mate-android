package com.goalpanzi.mission_mate.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MissionBoardResponse(
    val number : Int,
    val reward : BoardRewardResponse = BoardRewardResponse.NONE,
    val isMyPosition : Boolean = false,
    val missionBoardMembers : List<MissionBoardMemberResponse>
)
