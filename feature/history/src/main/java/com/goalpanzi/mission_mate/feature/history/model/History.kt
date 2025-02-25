package com.goalpanzi.mission_mate.feature.history.model

import com.goalpanzi.mission_mate.core.domain.common.model.user.CharacterType
import com.goalpanzi.mission_mate.core.domain.history.model.MissionHistory
import com.goalpanzi.mission_mate.core.domain.mission.model.MissionBoardMember
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class History(
    val missionId: Long,
    val description: String,
    val myVerificationCount: Int,
    val totalVerificationCount: Int,
    val rank: Int,
    val imageUrls: List<String>,
    val memberCount: Int,
    val missionMembers: MissionHistoryMembers,
    private val missionStartDate: String,
    private val missionEndDate: String,
) {
    val missionFormattedStartDate: String by lazy {
        formatDate(missionStartDate)
    }

    val missionFormattedEndDate: String by lazy {
        formatDate(missionEndDate)
    }

    private fun formatDate(date: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern(FORMATTER_PATTERN_ISO8601_SSSZ)
            val localDate = LocalDate.parse(date, formatter)
            DateTimeFormatter.ofPattern(FORMATTER_PATTERN, Locale.getDefault()).format(localDate)
        }catch (e: Exception){
            date
        }
    }

    companion object {
        private const val FORMATTER_PATTERN = "yyyy.MM.dd"
        private const val FORMATTER_PATTERN_ISO8601_SSSZ = "yyyy-MM-dd'T'HH:mm:ss" //"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }
}

class MissionHistoryMembers(
    private val members: List<CharacterType>
) {
    val extraNumbers = if(members.size > VISIBLE_MEMBERS_NUMBER){
        members.size - VISIBLE_MEMBERS_NUMBER
    } else {
        0
    }

    val distinctCharacters: List<CharacterType> by lazy {
        members.distinct().take(VISIBLE_MEMBERS_NUMBER)
    }

    companion object {
        private const val VISIBLE_MEMBERS_NUMBER = 3

        fun from(members: List<MissionBoardMember>): MissionHistoryMembers {
            return MissionHistoryMembers(
                members =  members.map {
                    it.characterType
                }
            )
        }
    }
}

fun MissionHistory.toUiModel() : History {
    return History(
        missionId = missionId,
        description = description,
        myVerificationCount = myVerificationCount,
        totalVerificationCount = totalVerificationCount,
        rank = rank,
        imageUrls = randomImageUrlList,
        memberCount = memberCount,
        missionStartDate = missionStartDate,
        missionEndDate = missionEndDate,
        missionMembers = MissionHistoryMembers.from(missionMembers)
    )
}
