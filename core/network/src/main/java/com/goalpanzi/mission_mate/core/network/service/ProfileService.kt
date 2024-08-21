package com.goalpanzi.mission_mate.core.network.service

import com.goalpanzi.core.model.request.SaveProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface ProfileService {
    @PATCH("/api/member/profile")
    suspend fun saveProfile(
        @Body request: SaveProfileRequest
    ): Response<Unit>
}