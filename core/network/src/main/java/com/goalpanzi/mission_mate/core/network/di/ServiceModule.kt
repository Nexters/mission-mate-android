package com.goalpanzi.mission_mate.core.network.di

import com.goalpanzi.mission_mate.core.network.service.HistoryService
import com.goalpanzi.mission_mate.core.network.service.LoginService
import com.goalpanzi.mission_mate.core.network.service.MissionService
import com.goalpanzi.mission_mate.core.network.service.OnboardingService
import com.goalpanzi.mission_mate.core.network.service.ProfileService
import com.goalpanzi.mission_mate.core.network.service.TokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideLoginService(
        @TokenRetrofit retrofit: Retrofit
    ): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileService(
        @TokenRetrofit retrofit: Retrofit
    ): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    @Singleton
    fun provideOnboardingService(
        @TokenRetrofit retrofit: Retrofit
    ): OnboardingService {
        return retrofit.create(OnboardingService::class.java)
    }

    @Provides
    @Singleton
    fun provideMissionService(
        @TokenRetrofit retrofit: Retrofit)
    : MissionService {
        return retrofit.create(MissionService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenService(
        @TokenReissueRetrofit retrofit: Retrofit
    ) : TokenService {
        return retrofit.create(TokenService::class.java)
    }

    @Provides
    @Singleton
    fun provideHistoryService(
        @TokenRetrofit retrofit: Retrofit
    ) : HistoryService {
        return retrofit.create(HistoryService::class.java)
    }
}
