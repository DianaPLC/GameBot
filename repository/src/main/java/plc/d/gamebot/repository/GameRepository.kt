package plc.d.gamebot.repository

import kotlinx.coroutines.flow.Flow
import plc.d.gamebot.util.Complexity
import plc.d.gamebot.util.Duration
import plc.d.gamebot.util.Size

interface GameRepository {
    val gameFlow: Flow<List<GameDto>>

    suspend fun insert(vararg games: GameDto)

    suspend fun update(vararg games: GameDto)

    suspend fun delete(vararg games: GameDto)

    suspend fun deleteGamesById(ids: Set<String>)

    suspend fun getGameById(id: String): GameDto

    suspend fun getGamesForFilters(
        players: Int,
        durations: List<Duration>,
        sizes: List<Size>,
        complexities: List<Complexity>,
        coops: List<Boolean>
    ): List<GameDto>
}