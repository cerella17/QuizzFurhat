package furhatos.app.cardgame.locales.english.decks

import furhatos.app.cardgame.flow.Nomatch
import furhatos.app.cardgame.flow.discussion.TakeInitiative
import furhatos.app.cardgame.game.Card
import furhatos.app.cardgame.game.Game
import furhatos.app.cardgame.game.deck
import furhatos.app.cardgame.locales.english.englishLocale
import furhatos.app.cardgame.options
import furhatos.nlu.common.DontKnow
import furhatos.nlu.common.Maybe
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val desertSurvivalDeckEnglish = deck {

    imgFolder = "desertSurvival"
    name = "Desert Survival Task"
    unitLabel = ""

    // In this task we don't want to show the truth values, as they are meaningless to the user
    // Instead, we show 1 - 5, reflecting their rank
    displayTruth = {card -> "${5 - Game.cardSet.correctOrder.indexOf(card)}"}

    input {
        singular += "item"

        min_def += "the least important item"
        min_def += "the least important thing"
        min_def += "the least important"

        max_def += "the most important item"
        max_def += "the most important thing"
        max_def += "the most important"

        is_min_def += options("is") / min_def

        is_max_def += options("is") / max_def

        is_less_than += "is less important than"

        is_more_than += "is more important than"
    }

    output {
        purpose = {
            +"In this game we have just crash landed in the desert. Our task is to rank these items according to their importance to our survival."
        }
        singular = {
            +"item"
        }
        min_def = {
            +"the least important item"
        }
        max_def = {
            +"the most important item"
        }
        is_min = {
            +"is the least important item"
        }
        is_max = {
            +"is the most important item"
        }
        is_less_than = {
            +"is less important than"
        }
        is_more_than = {
            +"is more important than"
        }
    }

    // Cards. There can be unlimited cards here, but only 5 are selected for each game. A good amount to get variation is minimum 8. Please use the Lion card as a template. It is a good practise to have some cards that are quite extreme, i.e really slow or really fast.

    card {
        id = "mirror"
        truth = 15
        image = "mirror.jpg"
        name = "Cosmetic Mirror"

        def = "the mirror"
        input += "mirror"
        input += "the mirror"

        argument_high += { +"The mirror would be very useful for signalling our presence and get help" }

        rhethoricalQuestion("Do you think the mirror could be used for anything?")

    }

    card {
        id = "top_coat"
        truth = 14
        image = "top_coat.jpg"
        name = "Top Coat"

        def = "the top coat"
        input += "coat"
        input += "top coat"
        input += "the top coat"

        argument_high += { +"I think the top coat can be used to protect you from the hot dry air" }
        argument_high += { +"I think the coat can be used to limit skin exposure to sun" }

        rhethoricalQuestion("Do you think the top coat could be used for anything?")

    }

    card {
        id = "water"
        truth = 13
        image = "water.jpg"
        name = "Bottle of water"

        def = "the water bottle"
        input += "water"
        input += "water bottle"
        input += "bottle of water"

        argument_high += { +"Having water to drink is very important to not get dehydrated" }
        argument_high += { +"Humans can't survive without water longer than a few days" }
        argument_low += { +"You could probably survive a few days without water" }
        argument_low += { +"One bottle of water is not going to last for very long" }
        argument_low += { +"I do not need water to survive" }

        rhethoricalQuestion("I have heard there is not much water in the desert")

    }

    card {
        id = "flashlight"
        truth = 12
        image = "flashlight.jpg"
        name = "Flashlight"

        def = "the flashlight"
        input += "flashlight"
        input += "the flashlight"

        argument_high += { +"A flashlight could be used to signal that we need help" }
        argument_high += { +"We could use the flashlight to move during the night when it is not so hot" }
        argument_low += { +"The flashlight might run out of battery" }
        argument_low += { +"Flashlight will not help us during the day" }
    }

    card {
        id = "parachute"
        truth = 11
        image = "parachute.jpg"
        name = "Parachute"

        def = "the parachute"
        input += "parachute"
        input += "the parachute"
        input += "the chute"

        argument_high += { +"The parachute could serve both as a shelter and a signaling device" }
        argument_high += { +"The parachute is colorful and can be spotted from a long distance" }
        argument_low += { +"I am not sure we need a parachute since we have already landed" }
        argument_low += { +"The parachute looks heavy and carrying it would be hard work" }
    }

    card {
        id = "jackknife"
        truth = 10
        image = "jackknife.jpg"
        name = "Jackknife"

        def = "the jackknife"
        input += "jackknife"
        input += "the jackknife"
        input += "knife"
        input += "the knife"
        input += "the blade"

        argument_high += { +"The jackknife would be useful for rigging a shelter" }
        argument_high += { +"The jackknife could perhaps be used to cut up a cactus to get some moisture" }
        argument_low += { +"Maybe we could use a sharp stone to cut things instead of the knife" }
        argument_low += { +"There is a risk that we will cut ourselves on the knife" }
        argument_low += { +"I can not think of a good use for the knife" }

    }

    card {
        id = "raincoat"
        truth = 9
        image = "raincoat.jpg"
        name = "Raincoat"

        def = "the raincoat"
        input += "raincoat"
        input += "the raincoat"

        argument_high += { +"The raincoat could be used to extract moisture from the air and get water to drink" }
        argument_low += { +"The raincoat is not a very convenient thing to wear in the desert" }

        rhethoricalQuestion("I don't think it is very likely to rain in the desert. Do you?")
    }

    card {
        id = "pistol"
        truth = 8
        image = "pistol.jpg"
        name = "Pistol"

        def = "the pistol"
        input += "pistol"
        input += "the pistol"
        input += "gun"
        input += "the gun"
        input += "the firearm"

        argument_high += { +"The pistol could be used to signal that we need help" }
        argument_low += { +"I don't think there is much to shoot at in the desert" }

        initiative {
            robot {+"Have you ever used a gun?"}
            user(Yes()) {
                robot {+"OK, as long as no-one got hurt"}
            }
            user(No()) {
                robot {+"Me neither, I don't have any hands, so it would be hard for me it use it"}
            }
            user {
                robot {+"I don't have any hands, so it would be hard for me it use it"}
            }
        }
    }

    card {
        id = "sunglasses"
        truth = 7
        image = "sunglasses.jpg"
        name = "Sunglasses"

        def = "the sunglasses"
        input += "sunglasses"
        input += "the sunglasses"
        input += "glasses"
        input += "the glasses"

        argument_high += { +"The intense sunlight of the desert could be a serious problem" }
        argument_high += { +"Wearing sunglasses in the desert could make things more comfortable" }
        argument_low += { +"Looking cool in a pair of sunglasses might not be our highest priority" }

        rhethoricalQuestion("Don't you think I would look cool in sunglasses?")

    }

    card {
        id = "compress_kit"
        truth = 6
        image = "compress_kit.jpg"
        name = "Compress kit"

        def = "the compress kit"
        input += "the compress kit"
        input += "compress kit"
        input += "compressor"

        argument_high += { +"The compress kit could perhaps be wrapped around your head to protect you from the sun" }
        argument_high += { +"The compress kit could perhaps be used as a rope" }
        argument_low += { +"I have heard that the risk of infection is very low in the desert climate" }
    }

    card {
        id = "compass"
        truth = 5
        image = "compass.jpg"
        name = "Compass"

        def = "the compass"
        input += "the compass"
        input += "compass"

        argument_high += { +"Maybe the compass could be used for signalling somehow" }
        argument_high += { +"We could use the compass to make sure we don't walk in circles" }
        argument_low += { +"The compass is probably of little use in the middle of the desert" }
    }

    card {
        id = "map"
        truth = 4
        image = "map.jpg"
        name = "Map"

        def = "the map"
        input += "the map"
        input += "map"

        argument_high += { +"Maybe the map could be used to start a fire or as toilet paper" }
        argument_high += { +"The map could be useful for navigation" }
        argument_low += { +"The map might mislead someone to start walking out in the desert" }
        argument_low += { +"They always say one should stay put if one gets lost, not sure if map is useful in this case" }

        rhethoricalQuestion("Are you good at reading maps?")

    }

    card {
        id = "book"
        truth = 3
        image = "book_edible_animals.jpg"
        name = "Book of edible animals"

        def = "the book of edible animals"
        input += "the book"
        input += "edible animals"
        input += "the book of edible animals"
        input += "book"
        input += "book about animals"
        input += "animal book"

        argument_high += { +"Maybe we should try to find some food" }
        argument_high += { +"The book could help us identify non-poisonous animals" }
        argument_low += { +"I think our main problem will be dehydration and not starvation" }
        argument_low += { +"I don't think reading a book in the desert will get us anywhere" }

        initiative {
            robot {+"Have you ever eaten any desert animals?"}
            user(Yes()) {
                robot {+"I hope it was tasty"}
            }
            user(No()) {
                robot {+"Me neither, but on the other hand, I don't eat anything, since I'm a robot"}
            }
            user {
                robot {+"I don't eat anything, I'm a robot"}
            }
        }

    }

    card {
        id = "vodka"
        truth = 2
        image = "vodka.jpg"
        name = "Bottle of Vodka"

        def = "the bottle of vodka"
        input += "vodka"
        input += "vodka bottle"
        input += "bottle of vodka"
        input += "the bottle of vodka"
        input += "alcohol"

        argument_high += { +"Maybe we can overcome our fears with the help of the vodka" }
        argument_high += { +"Perhaps the vodka could be used to start a fire" }
        argument_low += { +"Alcohol could easily lead to dehydration" }
        argument_low += { +"If we get drunk we will not be able to make very good decisions" }
        argument_low += { +"Alcohol is rarely the answer" }
    }

    card {
        id = "salt_tablets"
        truth = 1
        image = "salt_tablets.jpg"
        name = "Salt tablets"

        def = "the salt tablets"
        input += "salt"
        input += "salt tablets"
        input += "the salt tablets"

        argument_low += { +"I think eating salt could lead to dehydration" }
        argument_low += { +"I cannot think of a good use for the salt tablets" }
    }
}

fun Card.rhethoricalQuestion(text: String) {
    initiative(proceed = TakeInitiative()) {
        robot {+text}
        user(Yes(), Nomatch) {
            robot {+"yeah"}
        }
        user(No(), DontKnow(), Maybe()) {
            robot {+"I see"}
        }
    }
}
