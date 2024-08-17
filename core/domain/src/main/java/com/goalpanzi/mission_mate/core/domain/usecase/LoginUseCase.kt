package com.goalpanzi.mission_mate.core.domain.usecase

import com.goalpanzi.mission_mate.core.datastore.datasource.AuthDataSource
import com.goalpanzi.mission_mate.core.datastore.datasource.DefaultDataSource
import com.goalpanzi.mission_mate.core.domain.repository.AuthRepository
import com.luckyoct.core.model.UserProfile
import com.luckyoct.core.model.base.NetworkResult
import com.luckyoct.core.model.response.GoogleLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val authDataSource: AuthDataSource,
    private val defaultDataSource: DefaultDataSource
) {
    suspend fun requestGoogleLogin(email: String): GoogleLogin? {
        return when (val response = authRepository.requestGoogleLogin(email)) {
            is NetworkResult.Success -> {
                response.data.also {
                    authDataSource.setAccessToken(it.accessToken).first()
                    authDataSource.setRefreshToken(it.refreshToken).first()
                    defaultDataSource.setMemberId(it.memberId).first()
                    (it.nickname to it.characterType).let { (nickname, character) ->
                        if (nickname != null && character != null) {
                            defaultDataSource.setUserProfile(
                                UserProfile(nickname, character)
                            ).first()
                        }
                    }
                }
            }

            is NetworkResult.Error, is NetworkResult.Exception -> null
        }
    }

    fun isNewUser(): Boolean = runBlocking(Dispatchers.IO) {
        authDataSource.getAccessToken().first() == null
    }

    fun getCachedUserData(): UserProfile? = runBlocking(Dispatchers.IO) {
        defaultDataSource.getUserProfile().first()
    }
}