package com.goalpanzi.mission_mate.feature.history.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.goalpanzi.mission_mate.core.designsystem.component.StableImage
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorGray1_FF404249
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorGray3_FF727484
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorGray4_FFE5E5E5
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorGray5_FFF5F6F9
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorOrange_FFFF5732
import com.goalpanzi.mission_mate.core.designsystem.theme.ColorWhite_FFFFFFFF
import com.goalpanzi.mission_mate.core.designsystem.theme.MissionMateTypography
import com.goalpanzi.mission_mate.core.designsystem.theme.MissionmateTheme
import com.goalpanzi.mission_mate.core.domain.common.model.user.CharacterType
import com.goalpanzi.mission_mate.feature.history.R
import com.goalpanzi.mission_mate.feature.history.model.History
import kotlinx.coroutines.delay

private const val HISTORY_LIST_ITEM_IMAGE_INTERVAL = 3000L

@Composable
fun HistoryList(
    histories: List<History>,
    lazyListState: LazyListState,
    onHistoryClick: (History) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.background(
            color = ColorGray5_FFF5F6F9
        ),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = histories,
            key = { it.missionId }
        ) { history ->
            HistoryListItem(
                missionId = history.missionId,
                imageUrls = history.imageUrls,
                characters = history.missionMembers.distinctCharacters,
                extraNumbers = history.missionMembers.extraNumbers,
                title = history.description,
                startDate = history.missionFormattedStartDate,
                endDate = history.missionFormattedEndDate,
                boardProgressed = history.myVerificationCount,
                boardTotal = history.totalVerificationCount,
                rank = history.rank,
                lazyListState = lazyListState,
                onClick = {
                    onHistoryClick(history)
                }
            )
        }
    }
}

@Composable
fun HistoryListItem(
    missionId : Long,
    imageUrls: List<String>,
    characters: List<CharacterType>,
    extraNumbers: Int,
    title: String,
    startDate: String,
    endDate: String,
    boardProgressed: Int,
    boardTotal: Int,
    rank: Int,
    lazyListState: LazyListState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState {
        Int.MAX_VALUE
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val enabledAnimation by remember {
        derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo.any {
            it.key == missionId
        } }
    }

    LaunchedEffect(enabledAnimation) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
            while (enabledAnimation) {
                delay(HISTORY_LIST_ITEM_IMAGE_INTERVAL)
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    Column(
        modifier = modifier.background(
            color = ColorWhite_FFFFFFFF
        ).clickable {
            onClick()
        }
    ) {
        HistoryListItemImage(
            state = pagerState,
            imageUrls = imageUrls
        )
        HistoryListItemInfo(
            characters = characters,
            extraNumbers = extraNumbers,
            title = title,
            startDate = startDate,
            endDate = endDate,
            boardProgressed = boardProgressed,
            boardTotal = boardTotal,
            rank = rank
        )
    }
}

@Composable
fun HistoryListItemImage(
    imageUrls: List<String>,
    state: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = state,
        userScrollEnabled = false
    ) { index ->
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            model = imageUrls[index % imageUrls.size ],
            contentDescription = "history_image",
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun HistoryListItemInfo(
    characters: List<CharacterType>,
    extraNumbers: Int,
    title: String,
    startDate: String,
    endDate: String,
    boardProgressed: Int,
    boardTotal: Int,
    rank: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HistoryListItemInfoDetail(
                modifier = Modifier.weight(1f),
                characters = characters,
                extraNumbers = extraNumbers,
                title = title,
                startDate = startDate,
                endDate = endDate
            )
            if(rank == 1){
                StableImage(
                    modifier = Modifier
                        .width(85.dp)
                        .height(80.dp)
                        .align(
                            Alignment.Bottom
                        ),
                    drawableResId = com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_normal_trophy_gold
                )
            }
        }
        HistoryListItemInfoResult(
            boardProgressed = boardProgressed,
            boardTotal = boardTotal,
            rank = rank
        )
    }

}

@Composable
fun HistoryListItemInfoDetail(
    characters: List<CharacterType>,
    extraNumbers: Int,
    title: String,
    startDate: String,
    endDate: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HistoryListItemInfoDetailMembers(
            modifier = Modifier.padding(bottom = 4.dp),
            characters = characters,
            extraNumbers = extraNumbers
        )
        HistoryListItemInfoDetailTitle(
            title = title
        )
        HistoryListItemInfoDetailPeriod(
            startDate = startDate,
            endDate = endDate
        )
    }
}

@Composable
fun HistoryListItemInfoDetailMembers(
    characters: List<CharacterType>,
    extraNumbers: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HistoryListItemInfoDetailMembersCharacterList(
            characters = characters
        )
        Text(
            text = stringResource(
                id = R.string.history_list_item_member_count,
                extraNumbers
            ),
            style = MissionMateTypography.body_xl_bold,
            color = ColorGray1_FF404249
        )
    }
}

@Composable
fun HistoryListItemInfoDetailMembersCharacterList(
    characters: List<CharacterType>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        characters.forEachIndexed { index, characterType ->
            HistoryListItemInfoDetailMembersCharacterItem(
                modifier = Modifier.padding(
                    start = (index * 20).dp
                ),
                character = characterType
            )
        }
    }
}

@Composable
fun HistoryListItemInfoDetailMembersCharacterItem(
    character: CharacterType,
    modifier: Modifier = Modifier
) {
    StableImage(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                color = ColorWhite_FFFFFFFF
            )
            .border(
                width = 1.dp,
                color = ColorGray4_FFE5E5E5,
                shape = CircleShape
            )
            .padding(
                5.dp
            ),
        drawableResId = when (character) {
            CharacterType.CAT -> com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_cat_selected
            CharacterType.DOG -> com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_dog_selected
            CharacterType.RABBIT -> com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_rabbit_selected
            CharacterType.BEAR -> com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_bear_selected
            CharacterType.PANDA -> com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_panda_selected
            CharacterType.BIRD -> com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_bird_selected
            else -> com.goalpanzi.mission_mate.core.designsystem.R.drawable.img_rabbit_selected
        },
        contentScale = ContentScale.FillWidth
    )

}

@Composable
fun HistoryListItemInfoDetailTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = title,
        style = MissionMateTypography.title_xl_bold,
        color = ColorGray1_FF404249
    )
}

@Composable
fun HistoryListItemInfoDetailPeriod(
    startDate: String,
    endDate: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(
            id = R.string.history_list_item_period,
            startDate,
            endDate
        ),
        style = MissionMateTypography.body_md_regular,
        color = ColorGray3_FF727484
    )
}

@Composable
fun HistoryListItemInfoResult(
    boardProgressed: Int,
    boardTotal: Int,
    rank: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        HistoryListItemInfoResultBoard(
            progressed = boardProgressed,
            total = boardTotal
        )
        HistoryListItemInfoResultRank(
            rank = rank
        )
    }

}

@Composable
fun HistoryListItemInfoResultBoard(
    progressed: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(
            id = R.string.history_list_item_board_count,
            progressed,
            total
        ),
        style = MissionMateTypography.body_xl_bold,
        color = ColorOrange_FFFF5732
    )
}

@Composable
fun HistoryListItemInfoResultRank(
    rank: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(
            id = R.string.history_list_item_rank,
            rank
        ),
        style = MissionMateTypography.body_xl_regular,
        color = ColorGray1_FF404249
    )
}

@Preview
@Composable
private fun HistoryListItemInfoDetailMembersCharacterListPreview() {
    HistoryListItemInfoDetailMembersCharacterList(
        characters = listOf(
            CharacterType.CAT,
            CharacterType.DOG,
            CharacterType.BIRD
        )
    )
}

@Preview
@Composable
private fun HistoryListItemInfoDetailMembersCharacterItemPreview() {
    HistoryListItemInfoDetailMembersCharacterItem(
        character = CharacterType.CAT
    )
}

@Preview
@Composable
private fun HistoryListItemPreview() {
    MissionmateTheme {
        HistoryListItem(
            missionId = 1,
            imageUrls = listOf(""),
            characters = listOf(
                CharacterType.CAT,
                CharacterType.DOG,
                CharacterType.BIRD
            ),
            extraNumbers = 3,
            title = "매일 저녁 1시간 먹기",
            startDate = "2024.08.15",
            endDate = "2024.09.14",
            boardProgressed = 8,
            boardTotal = 10,
            rank = 1,
            lazyListState = rememberLazyListState(),
            onClick = {}
        )
    }
}
