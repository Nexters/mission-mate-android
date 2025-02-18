package com.goalpanzi.mission_mate.core.domain.history.model

import com.goalpanzi.mission_mate.core.domain.mission.model.MissionBoardMember

data class MissionHistory(
    val missionId: Long,
    val description: String,
    val missionStartDate: String,
    val missionEndDate: String,
    val myVerificationCount: Int,
    val totalVerificationCount: Int,
    val rank: Int,
    val randomImageUrlList: List<String>,
    val memberCount: Int,
    val missionMembers: List<MissionBoardMember>
)
