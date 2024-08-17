package com.goalpanzi.mission_mate.core.datastore.datasource

import com.luckyoct.core.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface DefaultDataSource {
    fun clearUserData() : Flow<Unit>
    fun setUserProfile(data: UserProfile) : Flow<Unit>
    fun getUserProfile() : Flow<UserProfile?>
    fun getViewedTooltip() : Flow<Boolean>
    fun setViewedTooltip() : Flow<Unit>
}