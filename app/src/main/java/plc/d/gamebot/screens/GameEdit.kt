package plc.d.gamebot.screens

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
 * View for editing a single game entry. Values update as they are changed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameEdit (
    gameId: String,
    fetchGame: suspend (String) -> GameDto,
    onGameUpdate: (GameDto) -> Unit,
    onDoneClick: (String) -> Unit,
    onListClick: () -> Unit,
    onBotClick: () -> Unit,
    getPermit: ActivityResultLauncher<Array<String>>,
    font: FontFamily
) {
    var gameDto by remember { mutableStateOf<GameDto?>(null) }
    LaunchedEffect(key1 = gameId) {
        gameDto = fetchGame(gameId)
    }

    getPermit.launch(
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    var durationExpanded by remember { mutableStateOf(false) }
    var complexityExpanded by remember { mutableStateOf(false) }
    var sizeExpanded by remember { mutableStateOf(false) }

    gameDto?.let {game ->
        var newImgUri: Uri? by remember { mutableStateOf(Uri.EMPTY) }
        var imgUri: Uri by remember { mutableStateOf(game.picture) }
        val pictureCapture = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success && newImgUri != null) {
                imgUri = newImgUri!!
                gameDto = game.copy(picture = imgUri).apply { onGameUpdate(this) }
            } else {
                Log.i("DEBUG", "NOT A CHANCE")
            }
        }
        val name = game.name.ifEmpty {
            stringResource(id = R.string.no_name)
        }
        MainScaffold(
            font = font,
            title = name,
            onListClick = onListClick,
            onBotClick = onBotClick,
            listSelected = false,
            botSelected = false,
            floatingButtonClick = { onDoneClick(game.id) },
            floatingButtonVector = Icons.Filled.Check,
            floatingButtonDescription = stringResource(R.string.done),
            actions = {}
        ) { paddingValues ->
            var minplay by remember { mutableStateOf(game.minPlayers) }
            var maxplay by remember { mutableStateOf(game.maxPlayers) }
            val widthDp = LocalConfiguration.current.screenWidthDp - 16
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    label = { Text(stringResource(R.string.name)) },
                    placeholder = { Text("loading...") },
                    value = game.name,
                    onValueChange = {
                        gameDto = game.copy(name = it).apply { onGameUpdate(this) }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Green,
                        focusedLabelColor = Color.Green,
                        unfocusedLabelColor = Color.Green
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Card (modifier = Modifier.width(widthDp.dp)) {
                    Row(modifier = Modifier
                        .padding(12.dp, 8.dp, 12.dp, 0.dp)
                        .fillMaxWidth()) {
                        Badge(
                            modifier = Modifier
                                .offset(-4.dp + ((minplay - 1).dp * ((widthDp - 24) / 12)))
                                .width(24.dp),
                            containerColor = Color.Green
                        ) {
                            Text(minplay.toString(), color = Color.Black)
                        }
                        Badge(
                            modifier = Modifier
                                .offset(-28.dp + ((maxplay - 1).dp * ((widthDp - 24) / 12)))
                                .width(24.dp),
                            containerColor = Color.Green
                        ) {
                            Text(maxplay.toString(), color = Color.Black)
                        }
                    }
                    RangeSlider(
                        value = minplay.toFloat()..maxplay.toFloat(),
                        valueRange = 1f..12f,
                        steps = 11,
                        onValueChange = { range ->
                            minplay = range.start.toInt()
                            maxplay = range.endInclusive.toInt()
                        },
                        onValueChangeFinished = {
                            gameDto = game.copy(minPlayers = minplay, maxPlayers = maxplay)
                                .apply {onGameUpdate(this)}
                        },
                        modifier = Modifier.padding(8.dp, 0.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Green,
                            activeTrackColor = Color.Green,
                            activeTickColor = Color.Green,
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = -10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.players),
                            fontFamily = font,
                            textAlign = TextAlign.Center,
                            color = Color.Green,
                            fontWeight = FontWeight(700),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .height(16.dp)
                        )
                    }
                }
                SelectRow(
                    widthDp = widthDp,
                    label = R.string.duration,
                    current = getString(game.duration),
                    expanded = durationExpanded,
                    expansion = { durationExpanded = it },
                    font = font
                ) {
                    Duration.values().forEach {
                        DropdownMenuItem(
                            text = { Text(getString(it)) },
                            onClick = {
                                gameDto = game.copy(duration = it).apply { onGameUpdate(this) }
                                durationExpanded = false
                            }
                        )
                    }
                }
                SelectRow(
                    widthDp = widthDp,
                    label = R.string.complexity,
                    current = getString(game.complexity),
                    expanded = complexityExpanded,
                    expansion = { complexityExpanded = it },
                    font = font
                ) {
                    Complexity.values().forEach {
                        DropdownMenuItem(
                            text = { Text(getString(it)) },
                            onClick = {
                                gameDto = game.copy(complexity = it).apply { onGameUpdate(this) }
                                complexityExpanded = false
                            }
                        )
                    }
                }
                SelectRow(
                    widthDp = widthDp,
                    label = R.string.size,
                    current = getString(game.size),
                    expanded = sizeExpanded,
                    expansion = { sizeExpanded = it },
                    font = font
                ) {
                    Size.values().forEach {
                        DropdownMenuItem(
                            text = { Text(getString(it)) },
                            onClick = {
                                gameDto = game.copy(size = it).apply { onGameUpdate(this) }
                                sizeExpanded = false
                            }
                        )
                    }
                }
                InputRow(widthDp = widthDp, label = R.string.coop, font = font) {
                    Switch(
                        checked = game.isCoop,
                        onCheckedChange = {
                            gameDto = game.copy(isCoop = it).apply { onGameUpdate(this) }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            checkedTrackColor = Color.Black,
                            checkedBorderColor = Color.Green
                        )
                    )
                }

                val currentContext = LocalContext.current
                Button (
                    onClick = {
                        val baseImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        val newImgValues = ContentValues().apply {
                            put(
                                MediaStore.Images.Media.DISPLAY_NAME,
                                "${game.id}-${System.currentTimeMillis()}.jpg"
                            )
                            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        }
                        imgUri = Uri.EMPTY
                        newImgUri =
                            currentContext.contentResolver.insert(baseImgUri, newImgValues)
                        if (newImgUri != null) {
                            pictureCapture.launch(newImgUri)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = imgUri.toString().isNotEmpty()
                        ) {
                            AsyncImage(
                                model = ImageRequest
                                    .Builder(currentContext)
                                    .data(imgUri)
                                    .build(),
                                contentDescription = game.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(16.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_photo_camera_24),
                            contentDescription = stringResource(R.string.update_picture),
                            tint = Color(0x8000ff00),
                            modifier = Modifier
                                .size(100.dp)
//                                    .padding(20.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape)
//                                    .background(Color(0xF000ff00))
                        )
                    }
                }
            }
        }
    }
}

/**
 * Reusable component for dropdown/select input.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRow(
    widthDp: Int,
    label: Int,
    current: String,
    expanded: Boolean,
    expansion: (Boolean) -> Unit,
    font: FontFamily,
    dropdown: @Composable (ColumnScope.() -> Unit)
) {
    InputRow(widthDp = widthDp, label = label, font = font) {
        AssistChip(
            onClick = { expansion(true) },
            label = { Text(current) },
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Green
                )
            },
            border = AssistChipDefaults.assistChipBorder(borderColor = Color.Green)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expansion(false) },
            content = dropdown
        )
    }
}

/**
 * Reusable component for general input.
 */
@Composable
fun InputRow(
    widthDp: Int,
    label: Int,
    font: FontFamily,
    input: @Composable (ColumnScope.() -> Unit)
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column (modifier = Modifier.height(48.dp)) {
            Text(
                text = stringResource(label),
                fontFamily = font,
                textAlign = TextAlign.End,
                color = Color.Green,
                fontWeight = FontWeight(700),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .requiredWidth((widthDp / 3).dp)
                    .padding(16.dp)
            )
        }
        Column(content = input)
    }
}