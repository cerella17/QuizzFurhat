package furhatos.app.cardgame.locales.english

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.Nomatch
import furhatos.app.cardgame.flow.Silent
import furhatos.app.cardgame.flow.user
import furhatos.flow.kotlin.StateDefinition
import furhatos.gestures.Gestures
import furhatos.nlu.intent

val englishChat: StateDefinition = {

    user(intent("how old are you", "tell me how old you are", "what is your age", "when were you invented", "when were you made", "when were you created", "when were you born")) {
        robot {
            random {
                +"Nice of you to ask!"
                +"I'm glad you asked!"
            }
            +Gestures.BigSmile
            random {
                +"Let's see."
                +"Let me think."
            }
            random {
                +"I was born in $furhatInventionYear, so i guess "
            }
            +"I am "
            random {
                +"just under "
                +"a little under "
                +"almost "
            }
            +"${(currentYear - furhatInventionYear)} years old."
            random {
                +"And how old are you?"
                +"What about you?"
            }
        }

        user {
            robot {
                +Gestures.Nod
                +delay(300)
                +"I'm usually younger than most people i speak with."
                +delay(300)
                +"Let's get back to the game though."
                +Gestures.Smile
            }
        }

        user(Silent) {
            robot { +"Age is just a number anyway. So let's get back to the game instead!"; +Gestures.Smile }
        }
    }

    user(intent("what is your name", "your name is", "what are you named", "who are you", "what are you called", "what can i call you")) {
        robot {
            if (counter("TellName") > 1) {
                random {
                    +"i thought i told you already,"
                    +"didn't i already tell you?"
                    +"don't you remember?"
                }
            }
            random {
                +"i am called"
                +"i'm"
                +"my name is"
            }
            +sayFurhat
            +Gestures.Smile
        }
    }

    user(
        intent(
            "Why is this not working?",
            "Well, this is not working",
            "There seems to be something wrong",
            "Come on, why is this not working?",
            "This robot sucks",
            "This game sucks"
        )
    ) {
        robot {
            random {
                +"Ah stop blaming me. If I am bad, it is because you made me bad"
                +"I am sick of this. Stop blaming me, its you who made me goddammit!"
            }
        }

        user(
            intent(
                "It wasn't me who made you",
                "I didn't make you",
                "I didn't create you",
                "I made you alright",
                "It isn't my fault"
            )
        ) {
            robot {
                random {
                    +"It's alright, it doesn't matter now anyway."
                    +"It's okay, don't worry about it."
                }
            }
        }

        user(Nomatch, Silent) {
            robot {
                random {
                    +"Let's move on with the game."
                    +"Let's just proceed with playing."
                    +"Let's get back to the game"
                }
            }
        }
    }

    user(
        intent("i hate you", "i don't like you",
            "you are annoying", "why are you so annoying", "this is annoying",
            "you are pissing me off"
        )
    ) {
        robot {
            +Gestures.ExpressDisgust
            random {
                +"Wow, I used to hate me too! We should be friends"
                +"Okay, let me take note of that"
                +"But you still can’t live without me"
                +"It’s totally fine that you don’t like me. Not everyone has good taste"
            }
            random {
                +"But that doesn't matter now. Let's get back to the game now"
                +"So let's just get back to the game and get this over with"
                +"In any case, let's get back to the game now."
            }
        }
    }
}