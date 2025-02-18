package com.goalpanzi.mission_mate.core.domain.history.model

data class MissionHistories(
    val hasNext: Boolean,
    val resultList: List<MissionHistory>
)
