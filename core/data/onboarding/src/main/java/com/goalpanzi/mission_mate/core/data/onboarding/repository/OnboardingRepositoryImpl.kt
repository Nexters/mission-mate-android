package com.goalpanzi.mission_mate.core.data.onboarding.repository

import com.goalpanzi.mission_mate.core.data.common.handleResult
import com.goalpanzi.mission_mate.core.data.onboarding.mapper.toModel
import com.goalpanzi.mission_mate.core.data.onboarding.mapper.toRequest
import com.goalpanzi.mission_mate.core.domain.common.DomainResult
import com.goalpanzi.mission_mate.core.domain.common.convert
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionDetail
import com.goalpanzi.mission_mate.core.domain.onboarding.model.CreateMissionBody
import com.goalpanzi.mission_mate.core.domain.onboarding.model.Missions
import com.goalpanzi.mission_mate.core.domain.onboarding.repository.OnboardingRepository
import com.goalpanzi.mission_mate.core.mission.mapper.toModel
import com.goalpanzi.mission_mate.core.network.model.request.JoinMissionRequest
import com.goalpanzi.mission_mate.core.network.service.OnboardingService
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingService: OnboardingService
) : OnboardingRepository {
    override suspend fun createMission(createMissionBody: CreateMissionBody): DomainResult<MissionDetail> = handleResult {
        onboardingService.createMission(createMissionBody.toRequest())
    }.convert {
        it.toModel()
    }

    override suspend fun getMissionByInvitationCode(invitationCode: String): DomainResult<MissionDetail> = handleResult {
        onboardingService.getMissionByInvitationCode(invitationCode)
    }.convert {
        it.toModel()
    }

    override suspend fun joinMission(invitationCode: String): DomainResult<Unit> =
        handleResult {
            onboardingService.joinMission(JoinMissionRequest(invitationCode))
        }

    override suspend fun getJoinedMissions(filter : String): DomainResult<Missions> = handleResult {
        onboardingService.getJoinedMissions(filter)
    }.convert { it.toModel() }
}
