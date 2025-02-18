package com.goalpanzi.mission_mate.core.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MissionHistoriesResponse(
    val hasNext: Boolean,
    val resultList: List<MissionHistoryResponse>
)
