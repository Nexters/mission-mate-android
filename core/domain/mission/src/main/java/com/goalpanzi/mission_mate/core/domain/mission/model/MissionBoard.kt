package com.goalpanzi.mission_mate.core.domain.mission.model

data class MissionBoard(
    val number : Int,
    val reward : BoardReward = BoardReward.NONE,
    val isMyPosition : Boolean = false,
    val missionBoardMembers : List<MissionBoardMember>
)
