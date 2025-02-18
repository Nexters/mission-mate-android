package com.goalpanzi.mission_mate.core.network.service

import com.goalpanzi.mission_mate.core.network.model.response.MissionHistoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryService {
    @GET("/api/missions/histories/me")
    suspend fun getMyMissionHistories(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 30,
    ): Response<MissionHistoriesResponse>
}
