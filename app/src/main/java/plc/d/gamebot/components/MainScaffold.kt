package plc.d.gamebot.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import plc.d.gamebot.R

/**
 * Defines the scaffold to use across all app views. Composable elements can be
 * passed in to define the top bar actions available and the main view contents.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScaffold (
    font: FontFamily,
    title: String,
    onListClick: () -> Unit,
    onBotClick: () -> Unit,
    listSelected: Boolean,
    botSelected: Boolean,
    floatingButtonClick: () -> Unit,
    floatingButtonVector: ImageVector,
    floatingButtonDescription: String,
    actions: @Composable RowScope.() -> Unit = {},
    contents: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontFamily = font, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.Green
                ),
                actions = actions
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = floatingButtonClick,
                shape = CircleShape,
                containerColor = Color.Green,
            ){
                Icon (
                    imageVector = floatingButtonVector,
                    contentDescription = floatingButtonDescription,
                    tint = Color.Black
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_videogame_asset_24),
                        contentDescription = stringResource(R.string.app_name),
                        tint = Color.Green
                    ) },
                    label = { Text(stringResource(R.string.app_name), fontFamily = font, color = Color.Green) },
                    selected = botSelected,
                    onClick = onBotClick
                )
                NavigationBarItem(
                    icon = { Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = stringResource(R.string.games),
                        tint = Color.Green
                    ) },
                    label = { Text(stringResource(R.string.games), fontFamily = font, color = Color.Green) },
                    selected = listSelected,
                    onClick = onListClick
                )
            }
        }
    ) { contents(it) }
}