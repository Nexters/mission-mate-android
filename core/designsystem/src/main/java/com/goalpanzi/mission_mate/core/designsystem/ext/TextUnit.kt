package com.goalpanzi.mission_mate.core.designsystem.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun dpToSp(dp: Dp) : TextUnit = with(LocalDensity.current) { dp.toSp() }
