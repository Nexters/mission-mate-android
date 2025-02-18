package com.goalpanzi.mission_mate.feature.board.model

import com.goalpanzi.mission_mate.core.domain.mission.model.MissionBoardMember

data class MissionBoardMemberUiModel(
    val nickname : String,
    val characterUiModel : CharacterUiModel,
 //   val memberId : Long
)

fun MissionBoardMember.toModel() : MissionBoardMemberUiModel {
    return MissionBoardMemberUiModel(
        nickname = nickname,
        characterUiModel = characterType.toCharacterUiModel(),
       // memberId = memberId
    )
}
