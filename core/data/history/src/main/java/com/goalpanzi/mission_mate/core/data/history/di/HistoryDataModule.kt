package com.goalpanzi.mission_mate.core.data.history.di

import com.goalpanzi.mission_mate.core.data.history.repository.HistoryRepositoryImpl
import com.goalpanzi.mission_mate.core.domain.history.repository.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HistoryDataModule {

    @Binds
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
}
