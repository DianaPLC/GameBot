package plc.d.gamebot.screens

import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.launch
import plc.d.gamebot.BotScreen
import plc.d.gamebot.GameDisplayScreen
import plc.d.gamebot.GameEditScreen
import plc.d.gamebot.GameListScreen
import plc.d.gamebot.GameViewModel

/**
 * Managing component for the different views available in the app, including "back"
 * functionality and launched effects needed for certain view loads
 */
@Composable
fun Ui (
    viewModel: GameViewModel,
    getPermit: ActivityResultLauncher<Array<String>>,
    botFont: FontFamily,
    onExit: () -> Unit,
) {
    BackHandler {
        viewModel.popScreen()
    }

    val games by viewModel.gameFlow.collectAsState(initial = emptyList())

    val uiScope = rememberCoroutineScope()

    when(val screen = viewModel.screen) {
        null -> onExit()

        GameListScreen -> GameList(
            games = games,
            selectedGameIds = viewModel.selectedGameIds,
            onGameSelect = { id -> viewModel.toggleSelection(id) },
            onGameClick = { id -> viewModel.pushScreen(GameDisplayScreen(id)) },
            onClearSelections = viewModel::clearSelections,
            onDeleteSelections = viewModel::deleteSelectedGames,
            onBotClick = { viewModel.pushScreen(BotScreen) },
            font = botFont
        ) {
            uiScope.launch {
                val id = viewModel.createGame()
                viewModel.pushScreen(GameEditScreen(id))
            }
        }
        is GameDisplayScreen -> GameDisplay(
            gameId = screen.id,
            fetchGame = viewModel::getGameById,
            onEdit = { id -> viewModel.pushScreen(GameEditScreen(id)) },
            onListClick = { viewModel.pushScreen(GameListScreen) },
            onBotClick = { viewModel.pushScreen(BotScreen) },
            font = botFont
        )
        is GameEditScreen -> GameEdit(
            gameId = screen.id,
            fetchGame = viewModel::getGameById,
            onGameUpdate = viewModel::updateGame,
            onDoneClick = { id -> viewModel.pushScreen(GameDisplayScreen(id)) },
            onListClick = { viewModel.pushScreen(GameListScreen) },
            onBotClick = { viewModel.pushScreen(BotScreen) },
            getPermit = getPermit,
            font = botFont
        )
        is BotScreen -> Bot(
            onListClick = { viewModel.pushScreen(GameListScreen) },
            onBotClick = { viewModel.pushScreen(BotScreen) },
            getFilteredGames = viewModel::getGames,
            font = botFont
        )
    }
}