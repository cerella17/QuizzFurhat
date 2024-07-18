package furhatos.app.cardgame.game

import java.util.ArrayList
import java.util.HashMap

class CardOrder(collection: Collection<Card>) : ArrayList<Card>(collection) {

    fun hasChanged(lastCardOrder: List<Card>): Boolean {
        forEachIndexed {i,c -> if (c != lastCardOrder[i]) return true}
        return false
    }

    fun getChanges(lastCardOrder: CardOrder): List<Card> {
        val changes = mutableListOf<Card>()
        val amount = HashMap<Card, Int>()
        for (index in 0 until this.size) {
            val item = this[index]
            val left: Any = if (index == 0) "FIRST" else this[index - 1]
            val right: Any = if (index == this.size - 1) "LAST" else this[index + 1]
            val lastIndex = lastCardOrder.indexOf(item)
            val lastLeft: Any = if (lastIndex == 0) "FIRST" else lastCardOrder[lastIndex - 1]
            val lastRight: Any = if (lastIndex == lastCardOrder.size - 1) "LAST" else lastCardOrder[lastIndex + 1]
            if (index != lastIndex && left != lastLeft && right != lastRight) {
                changes.add(item)
                amount[item] = Math.abs(index - lastIndex)
            }
        }
        changes.sortWith(Comparator { o1, o2 -> amount[o2]!! - amount[o1]!! })
        return changes
    }

    /**
     * Calculates a score for the order, based on Furhat's belief.
     */
    fun evaluate(): Double {
        var r = 0.0
        for (i in 0..3) {
            for (j in i + 1..4) {
                val e = get(j).greaterThan(get(i))
                r += e
            }
        }
        return r
    }

    override fun toString(): String {
        return (map {it.id}).toString()
    }

}

