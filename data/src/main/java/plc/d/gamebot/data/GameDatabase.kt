package plc.d.gamebot.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [GameEntity::class],
    exportSchema = false
)

abstract class GameDatabase: RoomDatabase() {
    abstract val dao: GameDAO
}