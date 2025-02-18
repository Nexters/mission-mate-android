package com.goalpanzi.mission_mate.core.data.history.mapper

import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistories
import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistory
import com.goalpanzi.mission_mate.core.mission.mapper.toModel
import com.goalpanzi.mission_mate.core.network.model.response.MissionHistoriesResponse
import com.goalpanzi.mission_mate.core.network.model.response.MissionHistoryResponse

fun MissionHistoryResponse.toModel(): MissionHistory = MissionHistory(
    missionId = missionId,
    description = description,
    missionStartDate = missionStartDate,
    missionEndDate = missionEndDate,
    myVerificationCount = myVerificationCount,
    totalVerificationCount = totalVerificationCount,
    rank = rank,
    randomImageUrlList = randomImageUrlList,
    memberCount = memberCount,
    missionMembers = missionMembers.map {
        it.toModel()
    }
)

fun MissionHistoriesResponse.toModel(): MissionHistories = MissionHistories(
    hasNext = hasNext,
    resultList = resultList.map {
        it.toModel()
    }
)
