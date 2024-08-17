package com.goalpanzi.mission_mate.core.domain.usecase

import com.goalpanzi.mission_mate.core.datastore.datasource.DefaultDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetViewedTooltipUseCase @Inject constructor(
    private val defaultDataSource: DefaultDataSource
) {
    operator fun invoke(): Flow<Boolean> = defaultDataSource.getViewedTooltip()
}