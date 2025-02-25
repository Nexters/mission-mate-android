package com.goalpanzi.mission_mate.feature.history.model

import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistories

data class Histories(
    val hasNext: Boolean = false,
    val resultList: List<History> = emptyList()
)

fun MissionHistories.toUiModel() : Histories {
    return Histories(
        hasNext = hasNext,
        resultList = resultList.map { it.toUiModel() }
    )
}
