package furhatos.app.cardgame.locales.english.decks

import furhatos.app.cardgame.flow.Nomatch
import furhatos.app.cardgame.flow.discussion.TakeInitiative
import furhatos.app.cardgame.flow.user
import furhatos.app.cardgame.focusStack
import furhatos.app.cardgame.game.deck
import furhatos.app.cardgame.locales.english.englishLocale
import furhatos.app.cardgame.nlu.CardEntity
import furhatos.app.cardgame.options
import furhatos.gestures.Gestures
import furhatos.nlu.Intent
import furhatos.nlu.common.DontKnow
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.nlu.intent
import furhatos.util.Language

val animalSpeedDeckEnglish = deck {

    imgFolder = "animals"
    name = "Animal speed"
    unitLabel = "km/h" // Might not be needed, for example if you have YEARS as the unit. Because you don't say "1980 year" or similar, "1980" is enough

    input {
        singular += "animal"

        min_def += options("the slowest") / options("animal", "one", "")
        max_def += options("the") / options("fastest", "quickest") / options("animal", "one", "")

        is_min_def += options("may be", "might be", "could be", "is") / min_def
        is_max_def += options("may be", "might be", "could be", "is") / max_def

        is_less_than += "is slower than"
        is_less_than += "is not as fast as"
        is_less_than += "is not as quick as"
        is_less_than += "is a snail compared to"
        is_less_than += "looks like it's standing still compared to"

        is_more_than += "is faster than"
        is_more_than += "is quicker than"
        is_more_than += "is lightning compared to"
        is_more_than += "could run circles around"
    }

    output {
        purpose = {
            random {
                +"Well. In this game we are going to sort these animals by"
                +"So. For this game we will sort these animals by"
                +"Okey, In this game we shall arrange these animals by"
            }
            random {
                +"how fast they can run"
                +"their running speed"
                +"their top speed"
                +"how fast they are"
            }
        }
        singular = {
            +"animal"
        }
        min_def = {random {
            +"the slowest animal"
            +"the slowest one"
        }}
        max_def = {random {
            +"the fastest animal"
            +"the fastest one"
        }}
        is_min = {random {
            +"is the slowest animal"
            +"is the slowest one"
        }}
        is_max = {random {
            +"is the fastest animal"
            +"is the fastest one"
        }}
        is_less_than = {
            +"is slower than"
        }
        is_more_than = {
            +"is faster than"
        }
    }

    questions {

        class AskHaveSeenAnimal(): Intent() {
            var card: CardEntity? = null
            override fun getExamples(lang: Language): List<String> {
                return listOf("have you ever seen a @card")
            }
        }

        user<AskHaveSeenAnimal> {
            val seenAnimal = it.card!!.card!!
            robot {
                +attend(seenAnimal.location)
                +{focusStack.prime(seenAnimal)}
                +"No, I don't think I have ever seen ${seenAnimal.indef}."
                +attend(users.current)
                +"Have you?"
            }
            user(Yes()) {
                robot {+"Wow, sounds interesting. Was ${seenAnimal.def} fast?"}
                user(Yes(), No(), DontKnow(), Nomatch, proceed = TakeInitiative()) {
                    robot {+"Okay, I see"}
                }
            }
            user(No(), DontKnow()) {
                robot {+"Well, then it might be hard for us to know how fast ${seenAnimal.def} is"}
            }
            user(intent("I have seen them on TV", "I think I have seen them on TV", "I have only seen them on film", "I have seen pictures of them")) {
                robot {+"Me too. But that's not the same thing as seeing them in real life, I guess."}
            }
        }

    }

    // Cards. There can be unlimited cards here, but only 5 are selected for each game. A good amount to get variation is minimum 8. Please use the Lion card as a template. It is a good practise to have some cards that are quite extreme, i.e really slow or really fast.

    card {
        id = "zebra"
        truth = 64
        image = "zebra.jpg"
        name = "Zebra"

        def = "the zebra"
        indef = "a zebra"
        input += "zebra"
        input += "the zebra"
        argument_low += { +"Zebras have stripes to camouflage colourblind predators" }
        argument_low += { +"Zebras have not been bred for speed" }
        argument_low += { +"Cars slow down at zebra-crossings, so zebras must be slow too" }
        argument_high += { +"The Zebra needs to be fast in order to escape predators" }
        argument_high += { +"Zebras can not be tamed and so must be incredibly fast" }
        argument_high += { +"people add stripes on fast cars. so all striped things must be fast" }

        initiative {
            robot {+"Have you ever seen a zebra?"}
            user(Yes()) {
                robot {+"That's really cool"}
            }
            user(No()) {
                robot {+"Me neither, maybe we could go to Safari together sometime"}
            }
            user {
                robot {+"Yeah"}
            }
        }

    }

    card {
        id = "lion"
        truth = 80 // The actual speed of the lion..
        image = "lejon.jpg" // The image of the card
        name = "Lion" // Displayed on card
        def = "the lion" // Definitive form ("bestämd form")
        indef = "a lion"

        // All different ways you can say this card in
        input += "lion"
        input += "the lion"
        argument_low += { +"I think lions seem a bit lazy" }
        argument_low += { +"I think lions like to sleep a lot during the day" }
        argument_low += { +"I think lions hunt in packs because they're too slow to hunt alone."}
        argument_high += { +"The lion needs be fast in order to hunt" }
        argument_high += { +"The lion looks hungry" }
        argument_high += { +"Lions are big cats and cats seem very fast" }
        argument_high += { +"Lions save energy by sleeping up to 22 hours a day" }
    }

    card {
        id = "Kangaroo"
        truth = 71
        image = "kanguru.jpg"
        name = "Kangaroo"
        def = "the kangaroo"
        indef = "a kangaroo"
        input += "kangaroo"
        input += "the kangaroo"
        argument_low += { +"I am not sure that jumping can get you up to speed" }
        argument_low += { +"Kangaroo feet look clumsy" }
        argument_low += { +"Surely jumping is not as effective as running" }
        argument_high += { +"Kangaroos are super strong." }
        argument_high += { +"I have heard that kangaroos can jump very quickly" }
        argument_high += { +"Having big feet could mean that the kangaroo is very fast" }
    }

    card {
        id = "tiger"
        truth = 65
        image = "tiger.jpg"
        name = "Tiger"
        def = "the tiger"
        indef = "a tiger"
        input += "tiger"
        input += "the tiger"
        argument_low += { +"tigers hunt by ambush so they don't need speed very much" }
        argument_low += { +"the tiger looks a bit lazy" }
        argument_low += { +"big cats are usually slow" }
        argument_high += { +"the tiger needs be fast in order to hunt" }
        argument_high += { +"the tiger is a predator and must be fast to feed" }
        argument_high += { +"tigers have really strong legs" }
        argument_high += { +"a punch from a tiger can kill you" }
    }

    card {
        id = "ostrich"
        truth = 70
        image = "struts.jpg"
        name = "Ostrich"
        def = "the ostrich"
        indef = "an ostrich"
        input += "ostrich"
        input += "the ostrich"
        argument_low += { +"those big birds have tiny legs" }
        argument_low += { +Gestures.Smile; +"the ostrich is not very aerodynamic" }
        argument_low += { +"the ostrich is the world’s largest bird." }
        argument_high += { +"ostriches have very long legs" }
        argument_high += { +"the ostrich can flap wings to go fast" }
        argument_high += { +"an ostrich can cover five meters in a single stride" }
        argument_high += { +"ostriches running is aided by having just two toes on each foot" }
    }

    card {
        id = "giraffe"
        truth = 60
        image = "giraff.jpg"
        name = "Giraffe"
        def = "the giraffe"
        indef = "a giraffe"
        input += "giraffe"
        input += "the giraffe"
        argument_low += { +"the giraffe looks a bit clumsy" }
        argument_low += { +"you wouldn't run fast on stilts" }
        argument_low += { +"the long giraffe neck must get in the way" }
        argument_high += { +"the giraffe has very long legs" }
        argument_high += { +"the giraffe has the longest stride in nature" }
        argument_high += { +"a giraffe stride can reach four and a half meters" }
    }

    card {
        id = "rhinoceros"
        truth = 55
        image = "_noshorning.jpg"
        name = "Rhinoceros"
        def = "the rhinoceros"
        indef = "a rhinoceros"
        input += "rhinoceros"
        input += "the rhinoceros"
        input += "rhino"
        input += "the rhino"
        argument_low += { +"The rhinoceros looks pretty strong, maybe it doesn't have to be very fast" }
        argument_low += { +"rhinos are too heavy" }
        argument_low += { +"a rhino can weigh over three tons" }
        argument_high += { +"Rhinos are really powerful" }
        argument_high += { +"The rhinoceros looks like it is made of pure muscle" }
    }

    card {
        id = "hippo"
        truth = 30
        image = "_flodhast.jpg"
        name = "Hippo"
        def = "the hippo"
        indef = "a hippo"
        input += "hippo"
        input += "the hippo"
        argument_low += { +"Hippos closest living relatives are whales" }
        argument_low += { +"The hippo is very, very heavy" }
        argument_low += { +"Hippos live in water" }
        argument_high += { +"Hippos are highly aggressive and unpredictable" }
        argument_high += { +"The hippo is among the most dangerous animals in the world" }
    }

    card {
        id = "elk"
        truth = 72
        image = "alg.jpg"
        name = "Elk"
        def = "the elk"
        indef = "an elk"
        input += "elk"
        input += "moose"
        input += "the elk"
        argument_low += { +"An elk can weigh half a ton" }
        argument_low += { +"The elk can be slowed down by its big antlers" }
        argument_low += { +"Elk have tiny legs and huge bodies" }
        argument_high += { +"The elk is often called the king of the forest" }
        argument_high += { +"elks are like giant horses with horns" }
        argument_high += { +"Elk are really graceful" }
    }

    card {
        id = "wolverine"
        truth = 48
        image = "_jarv.jpg"
        name = "Wolverine"
        def = "the wolverine"
        indef = "a wolverine"
        input += "wolverine"
        input += "the wolverine"
        argument_low += { +"Wolverines have really short legs" }
        argument_low += { +"The wolverine is not very big" }
        argument_low += { +"Skunk bear is another name for the wolverine and bears are not that fast" }
        argument_high += { +"Quickhatch is another name for the wolverine, maybe it is very quick" }
        argument_high += { +"I hear wolverines have adamantium skeletons" }
        argument_high += { +"Wolverines can travel 24 kilometers in a day" }
        argument_high += { +"Wolverine territories can range up to 600 kilometers" }
    }

    card {
        id = "wolf"
        truth = 66
        image = "varg.jpg"
        name = "Wolf"
        def = "the wolf"
        indef = "a wolf"
        input += "wolf"
        input += "wolves"
        input += "the wolf"
        argument_low += { +"Wolf packs surround their prey so they don't need speed" }
        argument_low += { +"A wolf can eat nine kilos of meat in one go" }
        argument_high += { +"Dogs have evolved from the wolf and can be very fast" }
        argument_high += { +"The wolf can roam large areas and long distances" }
        argument_high += { +"The wolf is a great hunter" }
    }

    card {
        id = "horse"
        truth = 88
        image = "hast.jpg"
        name = "Horse"
        def = "the horse"
        indef = "a horse"
        input += "horse"
        input += "the horse"
        // argument_low += { +"A horse's teeth take up a larger amount of space in their head than their brain" }
        argument_low += { +"Horses have been domesticated so long they are lazy" }
        argument_high += { +"Horses are used in racing" }
        argument_high += { +"Horses are bred to be fast" }
        argument_high += { +"Humans used horses for fast travelling before the invention of combustion engine" }
        argument_high += { +"Horses can run within hours after birth" }

        initiative {
            robot{+"Have you ever ridden a horse?"}
            user(Yes()) {
                robot {+"That's nice"}
            }
            user(No()) {
                robot {+"Me neither"}
            }
            user {
                robot {+"yeah"}
            }
        }
    }

    card {
        id = "slow_loris"
        truth = 4
        image = "loris.jpg"
        name = "Slow loris"
        def = "the slow loris"
        indef = "a slow loris"
        input += "slow loris"
        input += "the slow loris"
        input += "loris"
        argument_low += { +"The loris is quite lazy" }
        argument_low += { +"Having slow in your name should be a clue" }
        argument_low += { +"The name loris is Dutch and means clown" }
        argument_high += { +"The loris can travel eight kilometers in one night" }
        argument_high += { +"A loris home range can be the size of 35 football pitches" }
    }

    card {
        id = "polar_bear"
        truth = 40
        image = "polar_bear.jpg"
        name = "Polar bear"
        def = "the Polar bear"
        indef = "a Polar bear"

        input += "polar bear"
        input += "the polar bear"

        argument_low += { +"Polar bears can weigh 800 kilos" }
        argument_low += { +"I think Polar bears are quite calm" }
        argument_low += { +"Polar bears are fat, especially at the start of winter" }
        argument_high += { +"Polar bears need to be fast in order to escape the melting ice" }
        argument_high += { +"A polar bear need to be fast to catch their prey" }
        argument_high += { +"Polar bears can be three meters tall" }
    }

}