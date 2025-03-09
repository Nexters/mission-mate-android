package com.goalpanzi.mission_mate.feature.history.model

import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistories

data class Histories(
    val totalCount: Int = 0,
    val hasNext: Boolean = false,
    val resultList: List<History> = emptyList()
)

fun MissionHistories.toUiModel() : Histories {
    return Histories(
        totalCount = totalCount,
        hasNext = hasNext,
        resultList = resultList.map { it.toUiModel() }
    )
}
