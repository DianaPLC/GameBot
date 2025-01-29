package plc.d.gamebot.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import plc.d.gamebot.R
import plc.d.gamebot.components.ActionButton
import plc.d.gamebot.components.MainScaffold
import plc.d.gamebot.repository.GameDto
import plc.d.gamebot.util.getString

/**
 * View displaying the full game list.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameList (
    games: List<GameDto>,
    selectedGameIds: Set<String>,
    onGameSelect: (String) -> Unit,
    onGameClick: (String) -> Unit,
    onClearSelections: () -> Unit,
    onDeleteSelections: () -> Unit,
    onBotClick: () -> Unit,
    font: FontFamily,
    onGameCreate: () -> Unit
) {
    val selectedGameCount = selectedGameIds.count()
    MainScaffold(
        font = font,
        title = if (selectedGameCount > 0) {
            stringResource(R.string.selected, selectedGameCount.toString())
        } else {stringResource(R.string.games)},
        onListClick = { },
        onBotClick = onBotClick,
        listSelected = true,
        botSelected = false,
        floatingButtonClick = onGameCreate,
        floatingButtonVector =  Icons.Filled.Add,
        floatingButtonDescription = stringResource(R.string.new_game),
        actions = { if (selectedGameCount > 0) {
                        ActionButton(
                            onClick = onClearSelections,
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = R.string.clear_selections,
                        )
                        ActionButton(
                            onClick = onDeleteSelections,
                            imageVector = Icons.Default.Delete,
                            contentDescription = R.string.delete_selections,
                        )
                    }}
    ) { paddingValues ->
        if (games.isEmpty()) {
            Text(
                stringResource(id = R.string.no_games),
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            )
        } else {
            /**
             * Basic LazyColumn settings sourced from code in
             * /android-summer-2023/movies-ui2/app/.../components/ListScaffold.kt
             */
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(
                    items = games,
                    key = { it.id },
                ) {
                    val colors: Color
                    val iconColors: Color
                    if (it.id in selectedGameIds) {
                        colors = Color(0x4000ff00)
                        iconColors = Color.Black
                    } else {
                        colors = MaterialTheme.colorScheme.secondaryContainer
                        iconColors = Color.Green
                    }
                    Card(
                        elevation = CardDefaults.cardElevation(1.dp),
                        colors = CardDefaults.cardColors(colors),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { onGameClick(it.id) },
                                onLongClick = { onGameSelect(it.id) }
                            )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current).data(it.picture).build(),
                                contentDescription = it.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp, 100.dp)
                                    .padding(16.dp)
                                    .clip(CircleShape)
                            )
                            Column (verticalArrangement = Arrangement.Center) {
                                Row {
                                    Text(
                                        text = it.name,
                                        color = iconColors,
                                        fontFamily = font,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                }
                                Row (
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(getString(it.complexity))
                                    Text(getString(it.duration))
                                    Text(getString(it.size))
                                }
                                Row (
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                                        .fillMaxWidth()
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_people_alt_24),
                                            contentDescription = stringResource(
                                                id = R.string.players
                                            ),
                                        )
                                        Text(
                                            stringResource(
                                                R.string.players_range,
                                                it.minPlayers.toString(),
                                                it.maxPlayers.toString()
                                            )
                                        )
                                    }
                                    if (it.isCoop) { Text(stringResource(R.string.coop)) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}