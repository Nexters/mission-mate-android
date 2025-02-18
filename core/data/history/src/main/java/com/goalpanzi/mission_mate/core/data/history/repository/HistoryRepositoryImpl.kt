package com.goalpanzi.mission_mate.core.data.history.repository

import com.goalpanzi.mission_mate.core.data.common.handleResult
import com.goalpanzi.mission_mate.core.data.history.mapper.toModel
import com.goalpanzi.mission_mate.core.domain.common.DomainResult
import com.goalpanzi.mission_mate.core.domain.common.convert
import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistories
import com.goalpanzi.mission_mate.core.domain.history.repository.HistoryRepository
import com.goalpanzi.mission_mate.core.network.service.HistoryService
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyService: HistoryService
) : HistoryRepository {
    override suspend fun getMissionHistories(page: Int): DomainResult<MissionHistories> =
        handleResult {
            historyService.getMyMissionHistories(page)
        }.convert {
            it.toModel()
        }

}
