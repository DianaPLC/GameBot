package plc.d.gamebot.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plc.d.gamebot.data.GameDAO
import plc.d.gamebot.data.createDAO
import plc.d.gamebot.util.Complexity
import plc.d.gamebot.util.Duration
import plc.d.gamebot.util.Size

class GameDatabaseRepository (
    private val dao: GameDAO
): GameRepository {
    override val gameFlow: Flow<List<GameDto>> =
        dao.getGamesFlow().map {
            games -> games.map { it.toDto() }
        }

    override suspend fun insert(vararg games: GameDto) {
        dao.insert(*games.map {it.toEntity()}.toTypedArray())
    }

    override suspend fun update(vararg games: GameDto) {
        dao.update(*games.map {it.toEntity()}.toTypedArray())
    }

    override suspend fun delete(vararg games: GameDto) {
        dao.delete(*games.map {it.toEntity()}.toTypedArray())
    }

    override suspend fun deleteGamesById(ids: Set<String>) =
        dao.deleteGamesById(ids)

    override suspend fun getGameById(id: String): GameDto =
        dao.getGameById(id).toDto()

    override suspend fun getGamesForFilters(
        players: Int,
        durations: List<Duration>,
        sizes: List<Size>,
        complexities: List<Complexity>,
        coops: List<Boolean>
    ): List<GameDto> = dao.getGamesForFilters(
        players = players,
        durations = durations,
        sizes = sizes,
        complexities = complexities,
        coops = coops
    ).map { it.toDto() }

    companion object {
        fun create(context: Context) = GameDatabaseRepository(createDAO(context))
    }
}