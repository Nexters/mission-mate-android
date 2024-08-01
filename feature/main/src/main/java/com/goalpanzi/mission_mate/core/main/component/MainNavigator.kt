package com.goalpanzi.mission_mate.core.main.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.goalpanzi.mission_mate.core.navigation.RouteModel
import com.goalpanzi.mission_mate.feature.login.navigateToLogin
import com.goalpanzi.mission_mate.feature.onboarding.navigateToBoardSetup
import com.goalpanzi.mission_mate.feature.onboarding.navigateToInvitationCode
import com.goalpanzi.mission_mate.feature.onboarding.navigateToOnboarding

class MainNavigator(
    val navController: NavHostController
) {

    //TODO : change to Main
    val startDestination = RouteModel.Onboarding

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigationToOnboarding() {
        navController.navigateToOnboarding()
    }

    fun navigationToBoardSetup() {
        navController.navigateToBoardSetup()
    }

    fun navigationToInvitationCode() {
        navController.navigateToInvitationCode()
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController()
) : MainNavigator = remember(navController) {
    MainNavigator(navController)
}