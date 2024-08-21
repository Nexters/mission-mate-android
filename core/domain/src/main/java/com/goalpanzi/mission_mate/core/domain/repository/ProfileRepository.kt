package com.goalpanzi.mission_mate.core.domain.repository

import com.goalpanzi.mission_mate.core.network.ResultHandler
import com.goalpanzi.core.model.CharacterType
import com.goalpanzi.core.model.base.NetworkResult

interface ProfileRepository: ResultHandler {
    suspend fun saveProfile(nickname: String, type: CharacterType, isEqualNickname : Boolean): NetworkResult<Unit>
}