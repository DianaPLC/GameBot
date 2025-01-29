package plc.d.gamebot.data

import android.content.Context
import androidx.room.Room

fun createDAO(context: Context) = Room.databaseBuilder(
    context,
    GameDatabase::class.java,
    "Games"
).build().dao