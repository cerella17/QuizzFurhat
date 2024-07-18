package furhatos.app.cardgame.game

import furhatos.app.cardgame.options

class OptionList: GenericOptionList<String> {
    constructor(): super()
    constructor(init: List<String>): super(init)

    val select: String get() = randomAvoidRepeat() ?: "undefined"

    operator fun div(next: OptionList): OptionList {
        return OptionList(this.flatMap {thisItem ->
            next.map {nextItem ->
                join(thisItem, nextItem)
            }
        })
    }

    operator fun div(next: String): OptionList {
        return OptionList(this.map {thisItem ->
            join(thisItem, next)
        })
    }

    operator fun plus(next: OptionList): OptionList {
        return OptionList(this.toList() + next.toList())
    }

    private fun join(a : String, b: String) : String {
        return if (a.isEmpty() || b.isEmpty())
            a + b
        else
            "$a $b"
    }

}