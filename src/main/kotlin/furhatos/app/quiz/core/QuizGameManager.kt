package furhatos.app.quiz.core

import furhatos.app.quiz.core.questions.QuestionSet
import furhatos.app.quiz.setting.TeamEnum
import furhatos.records.User

object QuizGameManager {
    val QuestionSet = QuestionSet()
    var redLeader: UserData? = null
    var blueLeader: UserData? = null
    var currentTurnTeam: TeamEnum = TeamEnum.BLUE
    var round: Int = 0

    // editable
    var maxRounds: Int = 3
    var timeForQuestionTimeout: Int = 30000 // in ms

    fun nextTurn() {
        currentTurnTeam = if (currentTurnTeam == TeamEnum.RED) TeamEnum.BLUE else TeamEnum.RED
    }

    fun resetGame() {
        redLeader?.score = 0
        blueLeader?.score = 0
        currentTurnTeam = TeamEnum.BLUE
        round = 0
    }
}

data class UserData(
    var user: User,
    var name: String,
    var score: Int
)