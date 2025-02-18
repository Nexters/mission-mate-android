package com.goalpanzi.mission_mate.core.mission.mapper

import com.goalpanzi.mission_mate.core.data.common.mapper.toModel
import com.goalpanzi.mission_mate.core.domain.mission.model.BoardReward
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionBoard
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionBoardMember
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionBoards
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionDetail
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionRank
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionVerification
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionVerifications
import com.goalpanzi.mission_mate.core.network.model.response.BoardRewardResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionBoardMemberResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionBoardResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionBoardsResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionDetailResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionRankResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionVerificationResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionVerificationsResponse
import java.time.DayOfWeek


fun MissionBoardsResponse.toModel() : MissionBoards {
    return MissionBoards(
        missionBoards = missionBoards.map {
            it.toModel()
        },
        progressCount = progressCount,
        rank = rank
    )
}

fun MissionBoardResponse.toModel() : MissionBoard {
    return MissionBoard(
        number = number,
        reward = reward.toModel(),
        isMyPosition = isMyPosition,
        missionBoardMembers = missionBoardMembers.map {
            it.toModel()
        }
    )
}

fun MissionBoardMemberResponse.toModel() : MissionBoardMember {
    return MissionBoardMember(
        memberId = memberId,
        nickname = nickname,
        characterType = characterType.toModel()
    )
}

fun BoardRewardResponse.toModel() : BoardReward {
    return try{
        BoardReward.valueOf(this.name)
    }catch (e : Exception){
        BoardReward.NONE
    }
}

fun MissionVerificationsResponse.toModel() : MissionVerifications {
    return MissionVerifications(
        missionVerifications = missionVerifications.map {
            it.toModel()
        }
    )
}

fun MissionVerificationResponse.toModel() : MissionVerification {
    return MissionVerification(
        nickname = nickname,
        characterType = characterType.toModel(),
        imageUrl = imageUrl,
        verifiedAt = verifiedAt,
        viewedAt = viewedAt,
        missionVerificationId = missionVerificationId
    )
}

fun MissionRankResponse.toModel() : MissionRank {
    return MissionRank(
        rank = rank
    )
}

fun MissionDetailResponse.toModel() : MissionDetail {
    return MissionDetail(
        missionId = missionId,
        hostMemberId = hostMemberId,
        description = description,
        missionStartDate = missionStartDate,
        missionEndDate = missionEndDate,
        boardCount = boardCount,
        invitationCode = invitationCode,
        missionDays =  missionDays.map {
            DayOfWeek.valueOf(it)
        },
        timeOfDay = timeOfDay
    )
}
