package plc.d.gamebot.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import plc.d.gamebot.util.Complexity
import plc.d.gamebot.util.Duration
import plc.d.gamebot.util.Size

@Dao
abstract class GameDAO {
    @Insert
    abstract suspend fun insert(vararg games: GameEntity)

    @Update
    abstract suspend fun update(vararg games: GameEntity)

    @Delete
    abstract suspend fun delete(vararg games: GameEntity)

    @Query("DELETE FROM GameEntity WHERE id IN (:ids)")
    abstract suspend fun deleteGamesById(ids: Set<String>)

    @Query("SELECT * FROM GameEntity WHERE id = :id")
    abstract suspend fun getGameById(id: String): GameEntity

    @Query("SELECT * FROM GameEntity")
    abstract fun getGamesFlow(): Flow<List<GameEntity>>

    @Query(
        """
            SELECT * FROM GameEntity WHERE
            (maxPlayers >= :players AND minPlayers <= :players)
            AND duration IN (:durations)
            AND size IN (:sizes)
            AND complexity IN (:complexities)
            AND isCoop IN (:coops)
        """
    )
    abstract suspend fun getGamesForFilters(
        players: Int = 1,
        durations: List<Duration> = emptyList(),
        sizes: List<Size> = emptyList(),
        complexities: List<Complexity> = emptyList(),
        coops: List<Boolean> = emptyList()
    ): List<GameEntity>
}