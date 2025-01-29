package plc.d.gamebot.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import plc.d.gamebot.util.Complexity
import plc.d.gamebot.util.Duration
import plc.d.gamebot.util.Size
import java.util.UUID

@Entity
data class GameEntity (
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var minPlayers: Int,
    var maxPlayers: Int,
    var duration: Duration?,
    var size: Size?,
    var complexity: Complexity?,
    var isCoop: Boolean,
    var picture: String
)