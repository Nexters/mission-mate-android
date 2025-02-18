package com.goalpanzi.mission_mate.core.domain.history.repository

import com.goalpanzi.mission_mate.core.domain.common.DomainResult
import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistories

interface HistoryRepository {
    suspend fun getMissionHistories(page: Int) : DomainResult<MissionHistories>
}
