package com.goalpanzi.mission_mate.core.data.auth.repository

import com.goalpanzi.mission_mate.core.data.auth.mapper.toModel
import com.goalpanzi.mission_mate.core.data.common.DeviceInfoProvider
import com.goalpanzi.mission_mate.core.data.common.handleResult
import com.goalpanzi.mission_mate.core.datastore.datasource.AuthDataSource
import com.goalpanzi.mission_mate.core.domain.auth.repository.AuthRepository
import com.goalpanzi.mission_mate.core.domain.common.DomainResult
import com.goalpanzi.mission_mate.core.domain.common.convert
import com.goalpanzi.mission_mate.core.network.model.request.GoogleLoginRequest
import com.goalpanzi.mission_mate.core.network.service.LoginService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val authDataSource: AuthDataSource,
    private val deviceInfoProvider: DeviceInfoProvider
) : AuthRepository {

    override suspend fun requestGoogleLogin(email: String) = handleResult {
        val request = GoogleLoginRequest(
            email = email,
            deviceIdentifier = deviceInfoProvider.getDeviceSSAID()
        )
        loginService.requestGoogleLogin(request)
    }.convert {
        it.toModel()
    }

    override suspend fun requestLogout(): DomainResult<Unit> = handleResult {
        loginService.requestLogout()
    }

    override suspend fun requestAccountDelete(): DomainResult<Unit> = handleResult {
        loginService.requestDeleteAccount()
    }

    override fun getAccessToken(): Flow<String?> = authDataSource.getAccessToken()

    override fun getRefreshToken(): Flow<String?> = authDataSource.getRefreshToken()

    override fun setAccessToken(accessToken: String): Flow<Unit> =
        authDataSource.setAccessToken(accessToken)

    override fun setRefreshToken(refreshToken: String): Flow<Unit> =
        authDataSource.setRefreshToken(refreshToken)
}
