package plc.d.gamebot.repository

import android.net.Uri
import plc.d.gamebot.util.Complexity
import plc.d.gamebot.util.Duration
import plc.d.gamebot.data.GameEntity
import plc.d.gamebot.util.Size
import java.util.UUID

data class GameDto (
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val minPlayers: Int = 1,
    val maxPlayers: Int = 1,
    val duration: Duration? = null,
    val size: Size? = null,
    val complexity: Complexity? = null,
    val isCoop: Boolean = false,
    val picture: Uri = Uri.EMPTY
)

internal fun GameEntity.toDto() = GameDto(
    id = id,
    name = name,
    minPlayers = minPlayers,
    maxPlayers = maxPlayers,
    duration = duration,
    size = size,
    complexity = complexity,
    isCoop = isCoop,
    picture = Uri.parse(picture)
)

internal fun GameDto.toEntity() = GameEntity(
    id = id,
    name = name,
    minPlayers = minPlayers,
    maxPlayers = maxPlayers,
    duration = duration,
    size = size,
    complexity = complexity,
    isCoop = isCoop,
    picture = picture.toString()
)