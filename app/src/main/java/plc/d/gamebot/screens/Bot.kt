package plc.d.gamebot.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import plc.d.gamebot.R
import plc.d.gamebot.components.MainScaffold
import plc.d.gamebot.repository.GameDto
import plc.d.gamebot.util.Complexity
import plc.d.gamebot.util.Duration
import plc.d.gamebot.util.Size
import plc.d.gamebot.util.getString

/**
 * Defines a view for the user to select desired game
 * characteristics and generate a random suggested game.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bot (
    onListClick: () -> Unit,
    onBotClick: () -> Unit,
    getFilteredGames: suspend (Int,
                               List<Complexity>,
                               List<Duration>,
                               List<Size>,
                               Boolean,
                               Boolean) -> List<GameDto>,
    font: FontFamily
) {

    var complexityList by remember { mutableStateOf(List(Complexity.values().size) { false }) }
    var durationList by remember { mutableStateOf(List(Duration.values().size) { false }) }
    var sizeList by remember { mutableStateOf(List(Size.values().size) { false }) }
    var coop by remember { mutableStateOf(false) }
    var competitive by remember { mutableStateOf(false) }
    var players by remember { mutableStateOf(1) }

    var lookup by remember { mutableStateOf(0) }
    var eligibleGames: List<GameDto> by remember { mutableStateOf(emptyList()) }
    var selectedGame by remember { mutableStateOf<GameDto?>(null) }
    var noGameNotice by remember { mutableStateOf(false) }
    LaunchedEffect(lookup) {
        if (lookup > 0) {
            val complexities = Complexity.values().filterIndexed { idx, _ -> complexityList[idx] }
            val durations = Duration.values().filterIndexed { idx, _ -> durationList[idx] }
            val sizes = Size.values().filterIndexed { idx, _ -> sizeList[idx] }
            eligibleGames = getFilteredGames(
                players, complexities, durations, sizes, coop, competitive
            )
            if (eligibleGames.isEmpty()) {
                noGameNotice = true
            } else {
                selectedGame = eligibleGames.random()
            }
        }
    }

    MainScaffold(
        font = font,
        title = stringResource(id = R.string.app_name),
        onListClick = onListClick,
        onBotClick = onBotClick,
        listSelected = false,
        botSelected = true,
        floatingButtonClick = {
                    selectedGame = null
                    lookup++
                },
        floatingButtonVector = Icons.Filled.PlayArrow,
        floatingButtonDescription = stringResource(R.string.app_name),
        actions = { }
    ) { paddingValues ->
        val widthDp = LocalConfiguration.current.screenWidthDp - 16
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .width(widthDp.dp)
                    .padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.players),
                        textAlign = TextAlign.Center,
                        fontFamily = font,
                        color = Color.Green,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .height(16.dp)
                    )
                }
                Divider(color = Color.Green, modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier
                        .padding(12.dp, 8.dp, 12.dp, 0.dp)
                        .fillMaxWidth()
                ) {
                    Badge(
                        modifier = Modifier
                            .offset(-4.dp + ((players - 1).dp * ((widthDp - 24) / 12)))
                            .width(24.dp),
                        containerColor = Color.Green
                    ) {
                        Text(
                            players.toString(),
                            fontFamily = font,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Slider(
                    value = players.toFloat(),
                    valueRange = 1f..12f,
                    steps = 11,
                    onValueChange = { players = it.toInt() },
                    modifier = Modifier.padding(8.dp, 0.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Green,
                        activeTrackColor = Color.Green,
                        activeTickColor = Color.Green,
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.33f)
                        .padding(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.complexity),
                                textAlign = TextAlign.Center,
                                fontFamily = font,
                                color = Color.Green,
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Divider(color = Color.Green, modifier = Modifier.padding(8.dp))
                        Complexity.values().forEachIndexed { i, complexity ->
                            if (complexityList[i]) {
                                Text(getString(complexity), color = Color.Green)
                            } else {
                                Text(getString(complexity))
                            }
                            Switch(
                                checked = complexityList[i],
                                onCheckedChange = {
                                    complexityList =
                                        complexityList.mapIndexed { idx, v -> if (idx == i) !v else v }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Green,
                                    checkedTrackColor = Color.Black,
                                    checkedBorderColor = Color.Green
                                )
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.duration),
                                textAlign = TextAlign.Center,
                                fontFamily = font,
                                color = Color.Green,
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Divider(color = Color.Green, modifier = Modifier.padding(8.dp))
                        Duration.values().forEachIndexed { i, duration ->
                            if (durationList[i]) {
                                Text(getString(duration), color = Color.Green)
                            } else {
                                Text(getString(duration))
                            }
                            Switch(
                                checked = durationList[i],
                                onCheckedChange = {
                                    durationList =
                                        durationList.mapIndexed { idx, v -> if (idx == i) !v else v }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Green,
                                    checkedTrackColor = Color.Black,
                                    checkedBorderColor = Color.Green
                                )
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.size),
                                fontFamily = font,
                                textAlign = TextAlign.Center,
                                color = Color.Green,
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        Divider(color = Color.Green, modifier = Modifier.padding(8.dp))
                        Size.values().forEachIndexed { i, size ->
                            if (sizeList[i]) {
                                Text(getString(size), color = Color.Green)
                            } else {
                                Text(getString(size))
                            }
                            Switch(
                                checked = sizeList[i],
                                onCheckedChange = {
                                    sizeList =
                                        sizeList.mapIndexed { idx, v -> if (idx == i) !v else v }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Green,
                                    checkedTrackColor = Color.Black,
                                    checkedBorderColor = Color.Green
                                )
                            )
                        }
                    }
                }
            }
            Card(
                modifier = Modifier
                    .width(widthDp.dp)
                    .padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(12.dp, 8.dp, 12.dp, 0.dp)
                        .fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (coop) {
                            Text(stringResource(id = R.string.coop), color = Color.Green)
                        } else {
                            Text(stringResource(id = R.string.coop))
                        }
                        Switch(
                            checked = coop,
                            onCheckedChange = {
                                coop = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Green,
                                checkedTrackColor = Color.Black,
                                checkedBorderColor = Color.Green
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (competitive) {
                            Text(stringResource(id = R.string.competitive), color = Color.Green)
                        } else {
                            Text(stringResource(id = R.string.competitive))
                        }
                        Switch(
                            checked = competitive,
                            onCheckedChange = {
                                competitive = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Green,
                                checkedTrackColor = Color.Black,
                                checkedBorderColor = Color.Green
                            )
                        )
                    }
                }
            }
            androidx.compose.animation.AnimatedVisibility(visible = selectedGame?.name != null) {
                selectedGame?.let {
                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            IconButton(onClick = { selectedGame = null }) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = stringResource(R.string.done),
                                    tint = Color.Green
                                )
                            }
                        },
                        dismissButton = {
                            IconButton(onClick = {
                                selectedGame = null
                                lookup++
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = stringResource(R.string.done),
                                    tint = Color.Green
                                )
                            }
                        },
                        title = { Text(it.name, fontFamily = font, color = Color.Green) },
                        icon = {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current).data(it.picture)
                                    .build(),
                                contentDescription = it.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(200.dp)
                            )
                        }
                    )
                }
            }
            androidx.compose.animation.AnimatedVisibility(visible = noGameNotice) {
                AlertDialog(
                    onDismissRequest = { noGameNotice = false },
                    confirmButton = { IconButton(onClick = { noGameNotice = false }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.ok),
                            tint = Color.Green
                        )
                    } },
                    title = { Text(stringResource(R.string.no_games), fontFamily = font, color = Color.Green) },
                    text = { Text(stringResource(R.string.no_games_exp)) }
                )
            }
        }
    }
}