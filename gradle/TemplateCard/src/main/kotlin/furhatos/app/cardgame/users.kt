package furhatos.app.cardgame

import furhatos.flow.kotlin.UserDataDelegate
import furhatos.records.User

var User.name : String? by UserDataDelegate()
