package furhatos.app.cardgame.locales.english

import furhatos.app.cardgame.*
import furhatos.app.cardgame.flow.user
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.game.GameTable
import furhatos.app.cardgame.game.OptionList
import furhatos.app.cardgame.locales.english.decks.animalSpeedDeckEnglish
import furhatos.app.cardgame.locales.english.decks.desertSurvivalDeckEnglish
import furhatos.app.cardgame.locales.english.decks.inventionsDeckEnglish
import furhatos.app.cardgame.locales.english.decks.sugarInFoodEnglish
import furhatos.gestures.Gestures
import furhatos.gestures.Gestures.BigSmile
import furhatos.gestures.Gestures.BrowFrown
import furhatos.gestures.Gestures.BrowRaise
import furhatos.gestures.Gestures.Smile
import furhatos.gestures.Gestures.ExpressDisgust
import furhatos.gestures.Gestures.ExpressFear
import furhatos.gestures.Gestures.ExpressSad
import furhatos.gestures.Gestures.Oh
import furhatos.gestures.Gestures.Shake
import furhatos.gestures.Gestures.Thoughtful
import furhatos.nlu.SimpleIntent
import furhatos.util.Language

val englishLocale = locale(Language.ENGLISH_US) {

    decks = listOf(animalSpeedDeckEnglish, inventionsDeckEnglish, sugarInFoodEnglish, desertSurvivalDeckEnglish)

    input {
        ask_about_deck += "what do you think about @deck"
        ask_about_deck += "@deck"
        ask_about_deck += "is @deck a fun game"

        ask_favorite_game += options("what", "which") / options("", "game", "one") / options("do you like", "is your favorite", "do you think we should play", "do you recommend", "would you recommend")
        ask_favorite_game += "do you have any favorite"
        ask_favorite_game += "do you have any recommendation"

        // which animal do you think is the smallest
        ask_compare_cards += options("do you think") / "@card1" / (deck.input.is_less_than + deck.input.is_more_than) / "@card2"
        ask_compare_cards += "what do you think about @card1 and @card2"
        ask_compare_cards += "what about @card1 and @card2"
        ask_compare_cards += options("do you think it") / (deck.input.is_less_than + deck.input.is_more_than) / "@card2"

        assert_compare_cards += options("I think") / "@card1" / (deck.input.is_less_than + deck.input.is_more_than) / "@card2"

        ask_highest_card += options("which", "who", "what") / (deck.input.singular + options("one", "")) / options("do you think", "") /  deck.input.is_max_def
        ask_highest_card += options("do you think @card") / deck.input.is_max_def
        ask_highest_card += options("is @card") / deck.input.max_def
        ask_highest_card += options("I think @card") / deck.input.is_max_def
        ask_highest_card += options("The @card") / deck.input.is_max_def

        ask_lowest_card += options("which", "who", "what") / (deck.input.singular + options("one", "")) / options("do you think", "") / deck.input.is_min_def
        ask_lowest_card += options("do you think @card") / deck.input.is_min_def
        ask_lowest_card += options("is @card") / deck.input.is_min_def
        ask_lowest_card += options("I think @card") / deck.input.is_min_def
        ask_lowest_card += options("The @card") / deck.input.is_min_def

        comment_card += "what do you think about @card"
        comment_card += "do you have any opinions about @card"
        comment_card += "any ideas about @card"
        comment_card += "what about @card"

        game_done += options(
            "happy with this",
            "this is @finished", "this looks @finished", "think this is @finished", "i think @finished", "we're @finished", "i am @finished",
            "this is our @answer", "this is my @answer", "check @answer", "is this the @answer", "can see @answer", "show @answer", "what is @answer"
        )
        entity_finished_game += options("finished", "done", "bored", "good", "ok", "right")
        entity_game_solution += options("correct answer", "answer", "solution", "actual answer", "correct arrangement", "result")

        game_not_done += "we are not done"
        game_not_done += "this is not our solution"
        game_not_done += "we want to continue"
        game_not_done += "wait"
        game_not_done += "I want to change"

        request_why += "why do you think so"
        request_why += "why do you say that"
        request_why += "how come"

        request_opinion += "what do you think"
        request_opinion += "what is your opinion"
        request_opinion += "do you have any ideas"

        agree += "I agree"
        agree += "I agree with you"
        agree += "Sounds reasonable"
        agree += "you are right"
        agree += "Sure"
        agree += "Definitely"
        agree += "Probably"
        agree += "Absolutely"
        agree += "Ok"
        agree += "I guess"
        agree += "Fair enough"
        agree += "makes sense"

        disagree += "I don't agree"
        disagree += "I disagree"
        disagree += "I don't agree with you"
        disagree += "I disagree with you"
        disagree += "I don't think so"
        disagree += "I don't think this is correct"
        disagree += "I think you are wrong"
        disagree += "You are wrong"
        disagree += "Definitely not"
        disagree += "No"
        disagree += "No they are not"
        disagree += "makes no sense"
        disagree += "doesn't make any sense"
        disagree += "doesn't sound right"

        tell_name += "@name"
        tell_name += "my name is @name"
        tell_name += "I am @name"
        tell_name += "call me @name"

        play_again += "want to play again"
        play_again += "can we play again"
        play_again += "play another round"
        play_again += "once more"

    }

    output {
        button_play_again = "Play again"
        button_check_answer = "Check answer"
        button_tap_to_play = "Tap to play!"

        greeting = {
            +Smile
            random {
                +"Oh, time for a game!"
                +"Oh, you want to play a round of card game."
                +"You Wanna play I see!"
            }
            +delay(200)
            random {
                +"I'm still a bit sleepy, but I'll do my best."
                +"How nice, hope we make a good team"
                +"Let's see how good we can do."
            }
        }
        ask_name_1 = {
            random {
                +"What's your name?"
                +"Could you tell me your name?"
            }
            +BrowRaise
        }
        ask_name_2 = {
            random {
                +"And what is your name?"
                +"And your name is?"
                +"And who are you?"
                +"And you?"
            }
            +BrowRaise
        }
        acknowledge_name = {
            random {
                +"Nice meeting you, ${user.name ?: ""}"
                +"Hi ${user.name ?: "there"}, it is good to meet you"
                +"Hey ${user.name ?: "there"}"
                +"Pleasure meeting you, ${user.name}"
            }
            +Smile
        }
        my_name_is_furhat = {random {
            +"My name is $sayFurhat"
            +"You can call me $sayFurhat"
            +"I am $sayFurhat"
        }}
        answer_favorite_game = {
            random {
                +"I like all of them , but ${Game.decks.random().name} is perhaps my favorite"
                +"They are all good. If you haven't tried ${Game.decks.filter{it != Game.deck}.random().name}, you should give it a try"
            }
        }

        select_game = {
            +"Please select the game you would like to play"
        }
        select_new_game = {
            +"Please select the game you would like to play for this round"
        }
        confirm_same_game = {game-> {
            +"Okay, $game again. Well, why not."
        }}
        confirm_game = {game-> {
            +"Okay, let's play $game."
        }}
        propose_game = {game-> {
            random {
                +"$game is a fun game."
                +"$game is a great game."
                +"$game is an interesting game."
            }
            +"Tap on the deck if you want us to play that"
        }}
        propose_game_again = {game-> {
            random {
                +"$game is also a fun game."
                +"$game is another great game."
            }
        }}

        back_to_the_game_then = {
            +Smile
            random {
                +"Back to the game, then!"
                +"Back to the cards, then!"
                +"OK, Time to get back to the game!"
                +"Let's get back to the game shall we?"
            }
            delay(300)
        }
        explain_order = {
            +attend(GameTable.lowestLocation)
            // "The slowest one here, "
            include(deck.output.min_def)
            random {
                +"on the"
                +"to your"
            }
            +"left, "
            +Thoughtful
            +attend(GameTable.highestLocation)
            // "and there fastest one over here
            +"and"
            include(deck.output.max_def)
            random {
                +"to"
                +"on"
            }
            +"your right."
            +BrowRaise
        }
        by_the_way = {
            random {
                +"Just so you know, "
                +"Oh and by the way, "
                +"By the way, "
            }
            +BrowRaise
        }
        grab_attention = {
            random {
                +"hmm,"
                +"hey,"
            }
            +BrowRaise
        }
        explain_you_can_move_cards = {
            +"you need to "
            random {
                +"move the cards on the touchscreen."
                +"use your fingers to move the cards."
                +"move the cards on the screen with your fingers."
            }
            +Smile
            +delay(250)

            +"I don't have any hands,"
            +Smile
            +"you see"
            +delay(500)
        }
        explain_you_can_ask_about_cards = {
            +"i may not know much,".rate(1.1)
            +"but if you ask me about any card,"
            +Smile
            +"i'd be happy to share what i know."
        }
        explain_you_can_ask_to_end_game = {
            +"after a few rounds, when the cards look right, let me know and we can check the answer together."
        }
        ask_where_to_start = {
            +Smile
            +"let's start! "
            +"Which"
            +BrowFrown
            include(deck.output.singular)
            random {
                +"do you think we should "
                +"would you like to "
                +"should we "
            }
            +"start with?"
            +Smile
        }
        generic_motivation = {
            random {
                +"I don't know, it just feels right. Go with it will ya!"
                +"I'm not sure, it's just my gut feeling"
                +"It's just a guess"
            }
        }
        comment_order_better = {
            +Smile
            random {
                +"Now That's better"
                +"Now we are getting somewhere, finally!"
                +"Seems better"
                +"That's better"
                +"Looking better"
            }
        }
        comment_order_worse = {
            +ExpressDisgust
            random {
                +"Are you really sure about this?"
                +"Is this really better?"
                +"Really? You think that is better?"
                +"Are you confident this is right?"
                +"You sure this is right?"
            }
        }
        lets_continue_discussion = {
            random {
                +"Alright alright, we can continue for a bit more I guess!"
                +"Okay okay, we can keep going, I guess!"
                +"Hmmm, okay we try a bit longer, I guess!"
            }
        }
        ask_if_done = {
            random {
                +"Do you think we're done?"
                +"Shall we check if we got the correct answer?"
                +"Do you think you've arranged the cards correctly?"
            }
        }
        lets_check_solution = {
            random {
                +"okay, "
                +"alright, "
                +"sweet, "
                +"sure, "
                +"alright then! "
                +"I wonder how this went."
                +"I'm curious on our score."
                +"I hope we got some points."
            }
            random {
                +"let's check"
                +"let's bring up"
                +"let's look at"
                +"i'll fetch"
                +"let's have a look at"
            }
            random {
                +"the"
                +"our"
            }
            random {
                +"correct"
            }
            random {
                +"answer"
                +"solution"
                +"arrangement"
                +"result"
            }
        }
        dont_understand = {
            random {
                +"I'm not sure I"
                +"i don't"
                +"i'm afraid i don't"
            }
            +"understand"
            random {
                +"what you mean"
                +"that"
                +"what that means"
            }
        }

        no_more_ideas = {
            random {
                +"hmm, "
                +"no, "
                +Shake(strength=0.5)
                +ExpressSad(strength=2.0)
            }
            random {
                +"I am running out of ideas here!"
                +"I'm not sure how to proceed"
                +"I wonder, hmm, what next!"
                +"what do we do next, i wonder!"
                +"i wonder, what else can we do?"
                +"i can't think of anything else."
                +"nothing comes to mind."
                +"I have no more ideas"
                +"i'm out!"
                +"i'm out of ideas"
                +"no more ideas here, i'm afraid"
                +"i'm clueless now."
                +"i have no idea what to do next."
                +"I'm quite clueless."
                +"i'm quite lost!"
                +"honestly, i'm out of ideas."
                +"i've got nothing more to add!"
                +"what next? i don't know."
            }
        }
        hold_floor = {
            random {
                +"let's see"
                +"let me think"
                +"hmm"
                +"hmm... let's see"
                +"okay let's see"
            }
        }
        backchannel = {
            random {
                +"hmm"
            }
        }
        i_dont_know_which = {
            random {
                +"I have no idea which one"
                +"I am not sure which one"
            }
        }
        i_still_think_90 = {
            random {
                +"I am still super sure that"
                +"I am really sure that"
                +"Trust me! I am really sure that"
                +"I am still really convinced that"
            }
        }
        i_still_think_80 = {
            random {
                +"I still think that"
                +"I still sure that"
                +"I am still convinced that"
            }
        }
        i_think_95 = {
            random {
                +"I really think that"
                +"I am really sure that"
                // +"Trust me, I am damn sure that"
                +"I am convinced that"
            }
        }
        i_think_90 = {
            random {
                +"I'm quite sure that"
                +"I'm pretty sure that"
            }
        }
        i_think_80 = {
            random {
                +"I think that"
                +"I believe that"
            }
        }
        i_think_70 = {
            random {
                +"Perhaps"
                +"Maybe"
            }
        }
        i_think_correct = {
            random {
                +"I think it's correct that"
                +"I believe it's correct that"
                +"I think it's right that"
            }
        }
        i_dont_know_whether = {
            random {
                +"I have no idea whether"
                +"I have no clue whether"
                +"I do not know whether"
            }
        }
        suggest_move_card = {
            random {
                +"and"
                +"so"
            }
            random {
                +"I think"
                +"I guess"
            }
            random {
                +"we should move that card over here"
                +"the card should be moved over here"
                +"the card should probably be there"
            }
        }
        seek_agreement_name = {
            if (user.name != null) {
                random {
                    +"what do you think ${user.name}?"
                    +"tell me what you think ${user.name}!"
                    +"makes sense, ${user.name}?"
                    +"what do you say ${user.name}?"
                    +"sounds good, ${user.name}?"
                    +"sounds right, ${user.name}?"
                    +"you agree, ${user.name}?"
                    +"${user.name}, makes sense?"
                    +"${user.name}, what do you think?"
                    +"${user.name}, you agree?"
                    +"${user.name}, sounds good?"
                }
            } else include(seek_agreement)
        }
        seek_agreement = {
            random {
                +"what do you think?"
                +"tell me what you think?"
                +"makes sense?"
                +"does that make sense?"
                +"sounds good?"
                +"sounds right?"
                +"does that seem right to you?"
                +"what do you say?"
                +"you agree?"
            }
        }
        seek_agreement_accepted = {
            random {
                +"Great"
                +"Fantastic"
                +"Good"
                +"Awesome"
                +"Sweet"
                +"Excellent"
                +"Nice"
                +"sweet"
                +"super"
                +"lovely"
                +"brilliant"
                +"cool"
            }
        }
        seek_agreement_rejected = {
            +BrowRaise
            random {
                +"Really? So what do you think then?"
                +"So what is your opinion?"
                +"So what do you think?"
                +"Alright, so what's your suggestion?"
                +"Hmm, ok"
            }
        }
        request_move_card = {
            +"oh,"
            +Oh
            +delay(200)
            random {
                +"Could you move the card where it should be then?"
                +"Can you help me move the card please?"
                +"Let's reorder the cards on the screen, then?"
                +"Please arrange the cards like that."
                +"Let's move the card where it should be"
                +"Can you put the card in the right spot"
                +"Let's reorder the cards accordingly"
            }
            +Smile
        }
        request_opinion_name = {
            if (user.name != null) {
                +Thoughtful
                random {
                    +"hmm,"
                    +"ok,"
                    +"so, "
                }
                +"${user.name}, "
                random {
                    +"what do you think?"
                    +"what do you want to do?"
                    block {
                        +"which"
                        include(deck.output.singular)
                        +"next?"
                    }
                    +"what shall we do next?"
                    +"what next?"
                    +"any thoughts?"
                    +"any ideas?"
                    +"any other ideas?"
                    +"any more ideas?"
                }
                random {
                    +BrowRaise
                    +Smile
                }
            } else {
                include(request_opinion)
            }
        }
        request_opinion = {
            +Thoughtful
            random {
                +"hmm,"
                +"ok,"
                +"so, "
                +"alright, "
            }
            random {
                +"what do you think?"
                +"what do you want to do?"
                block {
                    +"which"
                    include(deck.output.singular)
                    +"next?"
                }
                +"what shall we do next?"
                +"what next?"
                +"any thoughts?"
                +"any ideas?"
                +"any other ideas?"
                +"any more ideas?"
            }
            random {
                +BrowRaise
                +Smile
            }
        }
        request_opinion_accept = {
            random {
                +"Yeah, let me know your thoughts"
                +"Tell me what you think"
            }
        }
        request_opinion_other = {
            if (user.name != null) random {
                +"${user.name}, what about you?"
                +"${user.name}, how about you?"
            }
            else random {
                +"What about you?"
                +"How about you?"
                +"and you?"
            }
        }
        request_opinion_unsure = {
            random {
                +"Yeah, this is tricky"
                +"Not always easy to make up your mind"
            }
        }
        and = "and"
        or = "or"
        either = "either"
        so = {
            +"so "
        }
        comment_score = {
            when (Game.cardSet.score) {
                1 ->  {
                    +ExpressSad
                    random {
                        +"One point! Perhaps you would have done better if I had kept sleeping"
                        +"Just One point! Well, I guess it's more than zero points"
                        +"Just a single point, now I feel a bit sad."
                    }
                }
                2 -> {
                    +ExpressSad
                    +"only two points!"
                    random {
                        +"I think we could do better"
                        +"Maybe i was too sleepy for this game!"
                        +"not so good perhaps."
                    }
                    +Shake
                }
                3 -> {
                    +Smile
                    random {
                        +"Three points! Am happy you woke me up though!"
                        +"Three points! not so bad!"
                        +"Almost perfect. But three points isn't too bad!"
                    }
                }
                5 -> {
                    random {
                        +"A perfect score!"
                        +"Five points, "
                    }
                    +BigSmile
                    random {
                        +"we are really a great team!"
                        +"We're great together! "
                    }
                }
                else -> {
                    +Shake
                    +"Not a single point!"
                    +ExpressDisgust
                    random {
                        +"Maybe I should have stayed asleep"
                        +"Well, I'm know for a fact we won't be worse next time"
                    }
                    +ExpressSad
                }
            }
        }
        i_told_you_that = {
            random {
                +"I told you that"
                +"I did tell you that"
            }
        }
        why_did_you_not_listen = {
            random {
                +"Turns out you should have listened to your new robot friend?"
                +"I would just like to point that out."
            }
            +Smile
        }
        i_thought_that = {
            random {
                +"I really thought that"
                +"I was so sure that"
            }
        }
        i_should_have_listened = {
            random {
                +"I am so sorry, I should have listen to you"
                +"Now I feel a bit stupid, I should have listened to you"
                +"I should have listened to you, my apologies"
            }
        }
        start_new_game = {
            random {
                +"So, which game should we play this time?"
            }
        }
        ask_play_again = {
            +"Do you want to play again?"
        }
        lets_play_again = {
            +"Sure, let's play again!"
        }
        alright = {
            random {
                +"alright, "
                +"okay, "
            }
        }
        comment_sleep = {
            when (Game.cardSet.score) {
                2 -> {
                    +"time for me to get some rest!"
                }
                3 -> {
                    +"i'll return to my nap then."
                }
                5 -> {
                    +"i'm going to dream about this game!"
                }
                else -> {
                    +"hope i don't get"; +ExpressFear; +"nightmares about this game.."
                }
            }
        }
        goodbye =  {
            random {
                +"It was nice playing with you. Goodbye!"
                +"Thanks for playing with me. See you!"
            }
        }
    }

    questions {
        user(SimpleIntent(examples = options("where should") /  deck.input.min_def / "be" + options("where should we place ") / deck.input.min_def)) {
            robot {
                +attend(GameTable.lowestLocation)
                include(deck.output.min_def)
                +"should be on your left"
            }
        }
        user(SimpleIntent(examples = options("where should") / deck.input.max_def / "be" + options("where should we place") / deck.input.max_def)) {
            robot {
                +attend(GameTable.highestLocation)
                include(deck.output.max_def)
                +"should be on your right"
            }
        }
        englishChat()
    }

}
