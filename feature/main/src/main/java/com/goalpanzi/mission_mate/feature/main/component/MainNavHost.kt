package com.goalpanzi.mission_mate.feature.main.component

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorWhite_FFFFFFFF
import com.goalpanzi.mission_mate.core.designsystem.util.isLightStatusBars
import com.goalpanzi.mission_mate.core.designsystem.util.setStatusBar
import com.goalpanzi.mission_mate.core.navigation.model.MainTabDataModel
import com.goalpanzi.mission_mate.core.navigation.model.RouteModel
import com.goalpanzi.mission_mate.feature.board.boardDetailNavGraph
import com.goalpanzi.mission_mate.feature.board.boardFinishNavGraph
import com.goalpanzi.mission_mate.feature.board.userStoryNavGraph
import com.goalpanzi.mission_mate.feature.board.verificationPreviewNavGraph
import com.goalpanzi.mission_mate.feature.login.loginNavGraph
import com.goalpanzi.mission_mate.feature.main.ext.isDarkStatusBarScreen
import com.goalpanzi.mission_mate.core.navigation.model.MainTabDataModelType
import com.goalpanzi.mission_mate.feature.onboarding.boardSetupNavGraph
import com.goalpanzi.mission_mate.feature.onboarding.boardSetupSuccessNavGraph
import com.goalpanzi.mission_mate.feature.onboarding.invitationCodeNavGraph
import com.goalpanzi.mission_mate.feature.profile.profileNavGraph
import com.goalpanzi.mission_mate.feature.setting.navigation.privacyPolicyNavGraph
import com.goalpanzi.mission_mate.feature.setting.navigation.servicePolicyNavGraph
import kotlin.reflect.typeOf

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    startDestination: RouteModel,
    padding: PaddingValues
) {
    val context = LocalContext.current
    val currentBackStackEntry by navigator.navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination

    LaunchedEffect(currentRoute) {
        if (currentRoute.isDarkStatusBarScreen()) {
            setStatusBar(context, false)
        } else if (!isLightStatusBars(context as Activity)) {
            setStatusBar(context, true)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ColorWhite_FFFFFFFF)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = startDestination
        ) {
            loginNavGraph(
                onLoginSuccess = {
                    if (it) navigator.navigationToMainTab(MainTabDataModel.Mission())
                    else navigator.navigateToProfileCreate()
                }
            )
            boardSetupNavGraph(
                onSuccess = {
                    navigator.navigationToBoardSetupSuccess()
                },
                onBackClick = {
                    navigator.popBackStack()
                }
            )
            boardSetupSuccessNavGraph(
                onClickStart = {
                    navigator.navigationToMainTab()
                }
            )
            invitationCodeNavGraph(
                onBackClick = {
                    navigator.popBackStack()
                },
                onNavigateMissionBoard = { missionId ->
                    navigator.navigationToMainTab()
                }
            )
            profileNavGraph(
                onSaveSuccess = {
                    navigator.navigationToMainTab(MainTabDataModel.Mission(true))
                },
                onBackClick = { navigator.popBackStack() }
            )
            servicePolicyNavGraph(
                onBackClick = { navigator.popBackStack() }
            )
            privacyPolicyNavGraph(
                onBackClick = { navigator.popBackStack() }
            )
            boardDetailNavGraph(
                onNavigateOnboarding = {
                    navigator.navigationToMainTab()
                },
                onBackClick = {
                    navigator.popBackStack()
                }
            )
            boardFinishNavGraph(
                onClickOk = {
                    navigator.navigationToMainTab(MainTabDataModel.History)
                }
            )
            userStoryNavGraph(
                onClickClose = {
                    navigator.popBackStack()
                }
            )
            verificationPreviewNavGraph(
                onClickClose = {
                    navigator.popBackStack()
                },
                onUploadSuccess = {
                    navigator.popBackStack()
                }
            )

            composable<RouteModel.MainTab>(
                typeMap = mapOf(typeOf<MainTabDataModel>() to MainTabDataModelType)
            ) { backStackEntry ->
                MainTabContent(
                    navigator = navigator,
                    mainTabDataModel = backStackEntry.toRoute<RouteModel.MainTab>().mainTabDataModel
                )
            }
        }
    }
}

