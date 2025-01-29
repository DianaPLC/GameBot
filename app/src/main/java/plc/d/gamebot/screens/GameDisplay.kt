package plc.d.gamebot.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import plc.d.gamebot.R
import plc.d.gamebot.components.DisplayField
import plc.d.gamebot.components.MainScaffold
import plc.d.gamebot.repository.GameDto
import plc.d.gamebot.util.getString

/**
 * Defines a detail view of a single game
 */
@Composable
fun GameDisplay (
    gameId: String,
    fetchGame: suspend (String) -> GameDto,
    onEdit: (String) -> Unit,
    onListClick: () -> Unit,
    onBotClick: () -> Unit,
    font: FontFamily
) {
    var gameDto by remember { mutableStateOf<GameDto?>(null) }
    LaunchedEffect(key1 = gameId) {
        gameDto = fetchGame(gameId)
    }
    gameDto?.let {game ->
        MainScaffold(
            font = font,
            title = game.name,
            onListClick = onListClick,
            onBotClick = onBotClick,
            listSelected = false,
            botSelected = false,
            floatingButtonClick = { onEdit(game.id) },
            floatingButtonVector = Icons.Filled.Edit,
            floatingButtonDescription = stringResource(R.string.edit_game),
            actions = {}
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                DisplayField(text = game.minPlayers.toString(), label = stringResource(R.string.min_players), labelFont = font)
                DisplayField(text = game.maxPlayers.toString(), label = stringResource(R.string.max_players), labelFont = font)
                DisplayField(text = getString(game.duration), label = stringResource(R.string.duration), labelFont = font)
                DisplayField(text = getString(game.complexity), label = stringResource(R.string.complexity), labelFont = font)
                DisplayField(text = getString(game.size), label = stringResource(R.string.size), labelFont = font)
                DisplayField(
                    text = if (game.isCoop) {
                        stringResource(id = R.string.yes)
                    } else stringResource(id = R.string.no),
                    label = stringResource(R.string.coop),
                    labelFont = font)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(game.picture).build(),
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

    }
}