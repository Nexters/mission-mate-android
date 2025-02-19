package com.goalpanzi.mission_mate.feature.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.goalpanzi.mission_mate.core.designsystem.theme.MissionmateTheme
import com.goalpanzi.mission_mate.core.domain.auth.usecase.LoginUseCase
import com.goalpanzi.mission_mate.core.navigation.model.RouteModel
import com.goalpanzi.mission_mate.feature.main.component.MainNavigator
import com.goalpanzi.mission_mate.feature.main.component.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var loginUseCase: LoginUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.WHITE, Color.WHITE)
        )
        val isNewUser = loginUseCase.isNewUser()
        val user = loginUseCase.getCachedUserData()

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()
            MissionmateTheme {
                MainScreen(
                    navigator = navigator,
                    startDestination = if (isNewUser) {
                        RouteModel.Login
                    } else {
                        if (user == null) {
                            RouteModel.Profile.Create
                        } else {
                            RouteModel.MainTab()
                        }
                    }
                )
            }
        }
    }

    companion object {
        const val NOTIFICATION_REQUEST_CODE = 1001
    }
}
