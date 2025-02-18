package com.goalpanzi.mission_mate.core.domain.history.usecase

import com.goalpanzi.mission_mate.core.domain.common.DomainResult
import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistories
import com.goalpanzi.mission_mate.core.domain.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMissionHistoriesUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(page: Int) : Flow<DomainResult<MissionHistories>> = flow {
        emit(historyRepository.getMissionHistories(page))
    }
}

