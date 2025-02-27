package com.goalpanzi.mission_mate.feature.history.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorGray1_FF404249
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorGray3_FF727484
import com.goalpanzi.mission_mate.core.designsystem.theme.MissionMateTypography
import com.goalpanzi.mission_mate.core.designsystem.theme.MissionmateTheme
import com.goalpanzi.mission_mate.feature.history.HistoryUiState
import com.goalpanzi.mission_mate.feature.history.HistoryViewModel
import com.goalpanzi.mission_mate.feature.history.R
import com.goalpanzi.mission_mate.feature.history.component.HistoryList
import com.goalpanzi.mission_mate.feature.history.model.Histories
import com.goalpanzi.mission_mate.feature.history.model.History

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryRoute(
    onHistoryClick: (History) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val historyUiState by viewModel.historyState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = historyUiState is HistoryUiState.Refreshing,
        onRefresh = viewModel::refresh
    )
    val shouldLoadingMore by remember {
        derivedStateOf { checkCanLoadMore(lazyListState, historyUiState) }
    }

    LaunchedEffect(Unit) {
        viewModel.initHistories()
    }

    LaunchedEffect(shouldLoadingMore) {
        if (shouldLoadingMore) {
            viewModel.fetchHistories()
        }
    }

    HistoryScreen(
        historyUiState = historyUiState,
        modifier = modifier,
        lazyListState = lazyListState,
        pullRefreshState = pullRefreshState,
        isRefreshLoading = historyUiState is HistoryUiState.Refreshing,
        onHistoryClick = onHistoryClick
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(
    historyUiState: HistoryUiState,
    lazyListState: LazyListState,
    pullRefreshState: PullRefreshState,
    isRefreshLoading : Boolean,
    onHistoryClick: (History) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HistoryTitle()

        when (historyUiState){
            is HistoryUiState.Success -> {
                HistoryListInfo(
                    modifier = Modifier.padding(bottom = 12.dp),
                    histories = historyUiState.histories
                )

                HistoryList(
                    histories = historyUiState.histories,
                    lazyListState = lazyListState,
                    pullRefreshState = pullRefreshState,
                    isRefreshLoading = isRefreshLoading,
                    onHistoryClick = onHistoryClick
                )
            }

            is HistoryUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> Unit
        }
    }
}

@Composable
fun HistoryTitle(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.history_title),
        modifier = modifier
            .wrapContentWidth()
            .padding(start = 24.dp, top = 20.dp, bottom = 16.dp, end = 24.dp),
        style = MissionMateTypography.heading_sm_bold,
        color = ColorGray1_FF404249
    )
}

@Composable
fun HistoryListInfo(
    histories: Histories,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HistoryListTitle(titleRes = R.string.history_list_title_complete)

        HistoryListCount(count = histories.resultList.size)

        HistoryListSort(modifier = Modifier.weight(1f))
    }
}

@Composable
fun HistoryListTitle(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(titleRes),
        style = MissionMateTypography.title_lg_bold,
        color = ColorGray1_FF404249
    )
}

@Composable
fun HistoryListCount(
    count: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.history_list_count, count),
        style = MissionMateTypography.body_lg_regular,
        color = ColorGray3_FF727484
    )
}

@Composable
fun HistoryListSort(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.history_list_sort_latest_finished),
        style = MissionMateTypography.body_lg_regular,
        color = ColorGray3_FF727484,
        textAlign = TextAlign.End
    )
}

private fun checkCanLoadMore(listState: LazyListState, historyUiState: HistoryUiState): Boolean {
    return if(historyUiState is HistoryUiState.Success){
        historyUiState.histories.hasNext && (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) >=
                (listState.layoutInfo.totalItemsCount - 1)
    }else {
        false
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun HistoryScreenPreview() {
    MissionmateTheme {
        HistoryScreen(
            historyUiState = HistoryUiState.Loading,
            lazyListState = rememberLazyListState(),
            pullRefreshState = rememberPullRefreshState(
                refreshing = false,
                onRefresh = {}
            ),
            isRefreshLoading = false,
            onHistoryClick = {}
        )
    }
}

