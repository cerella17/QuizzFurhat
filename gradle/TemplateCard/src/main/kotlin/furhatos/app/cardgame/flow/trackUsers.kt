package furhatos.app.cardgame.flow

import furhatos.app.cardgame.ActionPlayersUpdate
import furhatos.event.Event
import furhatos.flow.kotlin.*

fun FlowControlRunner.updateUsersOnGUI() {
    send(ActionPlayersUpdate(left = users.list.any {it.head.location.x < 0}, right = users.list.any {it.head.location.x > 0}))
}

val TrackUsers: State = state(null) {

    onUserEnter {
        updateUsersOnGUI()
    }

    onUserLeave {
        updateUsersOnGUI()
    }

}