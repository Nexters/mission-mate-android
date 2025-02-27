package com.goalpanzi.mission_mate.feature.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.goalpanzi.mission_mate.core.navigation.model.RouteModel.MainTabRoute.HistoryRouteModel
import com.goalpanzi.mission_mate.feature.history.screen.HistoryRoute

fun NavController.navigateToHistory(
    navOptions: NavOptions? = navOptions {
        popUpTo(this@navigateToHistory.graph.id) {
            inclusive = true
        }
    }
) {
    this.navigate(HistoryRouteModel.History, navOptions = navOptions)
}

fun NavGraphBuilder.historyNavGraph() {
    composable<HistoryRouteModel.History> {
        HistoryRoute(
            onHistoryClick = { history ->

            }
        )
    }
}
