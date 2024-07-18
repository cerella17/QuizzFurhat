package furhatos.app.quiz.setting

import furhatos.records.Record
import furhatos.records.User
import furhatos.skills.UserManager

// User variables
class SkillData(
    var scoreTeamRed : Int = 0,
    var scoreTeamBlue : Int = 0,
    var lastScoreTeamRed : Int = 0,
    var lastScoreTeamBlue : Int = 0,
    var interested : Boolean = true,
    var playing: Boolean = false,
    var played : Boolean = false,
    var namePlayerRed: String = "" ,
    var namePlayerBlue : String = "" ,
    var questionsAsked : MutableList<String> = mutableListOf(),
    var team : String = ""  // "blue" or "red"
) : Record()

val User.quiz : SkillData
    get() = data.getOrPut(SkillData::class.qualifiedName, SkillData())

// Custom user getters for convenience
fun UserManager.interested() = list.filter {
    it.quiz.interested && !it.quiz.playing
}

fun UserManager.playing() = list.filter {
    it.quiz.playing
}

fun UserManager.notQuestioned(question: String) = list.filter {
    it.quiz.playing && !it.quiz.questionsAsked.contains(question)
}

fun UserManager.blueTeam() = list.filter {
    it.quiz.team == "blue" && it.quiz.playing
}

fun UserManager.redTeam() = list.filter {
    it.quiz.team == "red" && it.quiz.playing
}

fun UserManager.nextPlaying(current: User): User {
    return if (current.quiz.team == "blue") {
        redTeam().firstOrNull() ?: current
    } else {
        blueTeam().firstOrNull() ?: current
    }
}