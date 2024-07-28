package furhatos.app.quiz.setting

import furhatos.records.Record
import furhatos.records.User

enum class TeamEnum {
    RED, BLUE
}

class GameData(
    var team: TeamEnum? = null,
    var score: Int = 0,
    var name: String = ""
) : Record()

val User.gameData: GameData
    get() = data.getOrPut(GameData::class.qualifiedName, GameData())
