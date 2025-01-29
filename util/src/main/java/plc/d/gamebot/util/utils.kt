package plc.d.gamebot.util


enum class Duration {
    SHORT, MEDIUM, LONG;
}

enum class Size {
    COMPACT, NORMAL, LARGE;
}

enum class Complexity {
    SIMPLE, AVERAGE, COMPLEX;
}

fun getString(current: Any?) =
    current?.toString()?.lowercase()?.replaceFirstChar { c -> c.uppercase() } ?: "--"