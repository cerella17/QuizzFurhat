package furhatos.app.cardgame.game

import furhatos.util.RandomList

open class GenericOptionList<T> : ArrayList<T> {
    constructor(): super()
    constructor(init: List<T>): super(init)

    private val used = mutableSetOf<T>()
    private var randomList = RandomList(size)

    private var lastToken: Any = Any()

    fun randomNoRepeat(token: Any) : T? {
        if (token != lastToken) {
            used.clear()
            lastToken = token
        }
        val iterated = mutableListOf<T>()
        while (true) {
            val item : T = randomAvoidRepeat() ?: return null
            if (!used.contains(item)) {
                used.add(item)
                return item
            }
            if (iterated.count { it == item } > 1)
                return null
            iterated.add(item)
        }
    }

    /**
     * Returns a random value from the list, avoiding repetition if possible
     */
    fun randomAvoidRepeat() : T? {
        if (isEmpty())
            return null
        else if (size == 1)
            return this[0]
        else {
            if (size != randomList.size())
                randomList = RandomList(size)
            return this[randomList.next()]
        }
    }

    fun randomAvoidRepeat(default: T) : T? {
        return randomAvoidRepeat()?:default
    }

    override fun toString(): String {
        return randomAvoidRepeat()?.toString()?:"undefined"
    }
}