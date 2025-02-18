package com.goalpanzi.mission_mate.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MissionHistoryResponse(
    val missionId: Long,
    val description: String,
    val missionStartDate: String,
    val missionEndDate: String,
    val myVerificationCount: Int,
    val totalVerificationCount: Int,
    val rank: Int,
    val randomImageUrlList: List<String>,
    val memberCount: Int,
    val missionMembers: List<MissionBoardMemberResponse>,
)
