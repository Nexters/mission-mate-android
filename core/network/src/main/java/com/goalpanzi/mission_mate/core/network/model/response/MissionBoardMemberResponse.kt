package com.goalpanzi.mission_mate.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MissionBoardMemberResponse(
    val memberId : Long,
    val nickname : String,
    val characterType : CharacterTypeResponse = CharacterTypeResponse.RABBIT
)
