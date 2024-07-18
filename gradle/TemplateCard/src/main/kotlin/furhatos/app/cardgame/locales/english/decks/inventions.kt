package furhatos.app.cardgame.locales.english.decks

import furhatos.app.cardgame.game.deck
import furhatos.app.cardgame.locales.english.englishLocale
import furhatos.app.cardgame.options
import furhatos.gestures.Gestures
import furhatos.nlu.common.DontKnow
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.nlu.intent

val inventionsDeckEnglish = deck {

    imgFolder = "inventions"
    name = "Inventions"
    unitLabel = ""

    input {
        singular += "invention"

        min_def += options("the") / options("earliest", "first", "oldest") / options("invention", "one", "")
        max_def += options("the") / options("most recent", "latest") / options("invention", "one", "")

        is_min_def += options("is", "might be", "may be", "was", "could be") / min_def
        is_max_def += options("is", "might be", "may be", "was", "could be") / max_def
        is_max_def += "was invented most recently"

        is_less_than += "is earlier than"
        is_less_than += "was invented earlier than"
        is_more_than += "is younger than"

        is_more_than += "is later than"
        is_more_than += "was invented later than"
        is_less_than += "is older than"
        is_more_than += "is more modern than"
    }

    output {
        purpose = {
            +"In this game we are supposed to sort these inventions by when they were invented"
        }
        singular = {
            +"invention"
        }
        min_def = {
            +"the earliest invention"
        }
        max_def = {
            +"the most recent invention"
        }
        is_min = {
            +"is the earliest invention"
        }
        is_max = {
            +"is the most recent invention"
        }
        is_less_than = {
            +"was earlier than"
        }
        is_more_than = {
            +"is more recent than"
        }

        card {
            id = "wheel"
            truth = -3500
            image = "hjulet.jpg"
            name = "wheel"
            def = "the wheel"

            input += "wheel"
            input += "the wheel"

            argument_low += { +"One shouldn't reinvent the wheel, I've heard. Sounds like an old saying to me" }
            argument_low += { +"Many people use the wheel as a comparison when referring to very old things" }
            argument_low += { +"Without the wheel, many inventions would not have been possible" }
        }

        // Cards. There can be unlimited cards here, but only 5 are selected for each game. A good amount to get variation is minimum 8. Please use the Lion card as a template. It is a good practise to have some cards that are quite extreme, i.e really slow or really fast.
        card {
            id = "champagne"
            truth = 1679
            image = "champagne.jpg"
            name = "champagne"

            def = "the champagne"
            input += "champagne"
            input += "the champagne"
            input += "the bubbly"

            argument_low += { +"Champagne is usually served in traditional settings" }
            argument_low += { +"Champagne is considered an acquired taste" }
            argument_high += { +"As a social robot, I get invites to events where they serve champagne. And I'm pretty new." }

            initiative {
                robot {+"Do you like champagne?"}
                user(Yes()) {
                    robot {+"Cool, me too!"}
                }
                user(No()) {
                    robot {+"Me neither, I am more of a beer guy"}
                }
                user {
                    robot {+"Yeah"}
                }
            }

        }

        card {
            id = "sandwich"
            truth = 1762 // The actual speed of the lion..
            image = "sandwich.jpg" // The image of the card
            name = "sandwich" // Displayed on card
            def = "the sandwich" // Definitive form ("bestämd form")

            // All different ways you can say this card in
            input += "sandwich"
            input += "the sandwich"

            argument_low += { +"it does not require rocket science to make a sandwich" }
            argument_low += { +"Sandwich is just things on things, not really a complex invention in my mind" }
            argument_high += { +"I don't think my great great grand parents knew what a sandwich was" }
            argument_high += { +"Subway sells sandwiches and subways are quite recent" }
        }

        card {
            id = "canned food"
            truth = 1809
            image = "canned_food.jpg"
            name = "canned food"
            def = "the canned food"
            input += "canned food"
            input += "the canned food"
            input += "food in can"
            input += "cans"

            argument_low += { +"People have been conserving food like forever, and how could have they without cans" }
            argument_high += { +"I think cans need to be made with modern machines" }
            argument_high += { +"Cans are made using modern machinery, they cant be that old" }
        }

        card {
            id = "piano"
            truth = 1700
            image = "piano.jpg"
            name = "piano"
            def = "the piano"
            input += "piano"
            input += "the piano"

            argument_low += { +"The piano feels like the mother of instruments" }
            argument_low += { +"Didn't Beethoven and his friends compose their famous symphonies on pianos?" }
            argument_low += { +"A lot of old classical music has been composed for pianos" }
            argument_high += { +"Kraftwerk, one of my favorite bands, used electric pianos a lot. And they are fairly modern" }
            argument_high += { +"Pianos can't be that old if they are still actively used today" }
        }

        card {
            id = "instant noodles"
            truth = 1958
            image = "instant_noodles.jpg"
            name = "instant noodles"
            def = "the instant noodles"
            input += "instant noodles"
            input += "the instant noodles"

            argument_high += { +"The word instant implies that it was invented in our present, quite stressful time" }
            argument_high += { +"A lot of modern materials are used to package instant noodles" }
        }

        card {
            id = "pasta"
            truth = 1154
            image = "pasta.jpg"
            name = "pasta"
            def = "the pasta"
            input += "pasta"
            input += "the pasta"

            argument_low += { +"Pasta is from Italy and italy used to be the center of the roman empire. They probably had pasta then as well." }
            argument_high += { +"Pasta can't be very old, I hear it is still very popular" }
        }


        card {
            id = "key"
            truth = -2000
            image = "nyckeln.jpg"
            name = "key"
            def = "the key"
            input += "key"
            input += "the key"

            argument_low += { +"People must have had the need to lock the door for a long time" }
            argument_low += { +"As long as there has been wealth there have been thieves. And as long as there have been thieves there have been locks and keys" }
            argument_high += { +"Keys need precision tools to be made" }

            initiative {
                robot {+"Do you think the first keys looked like the one on the picture?"}
                    user(Yes()) {
                        robot {+"Yeah, me too"}
                    }
                    user(No()) {
                        robot {+"Me neither. They probably were even less advanced"}
                    }
                    user {
                        robot {+"Nowadays people use key-cards instead. Not as interesting if you ask me"}
                    }
            }

        }

        card {
            id = "soap"
            truth = -1500
            image = "tvalen.jpg"
            name = "The Soap"

            def = "the soap"
            input += "soap"
            input += "the soap"

            argument_low += { +"I wonder how humans washed themselves before soap was a thing. And people have been around for a while" }
            argument_low += { +"Soap was originally made from animal fat, sounds barbaric to me" }
        }

        card {
            id = "glasses"
            truth = 1315
            image = "glasogonen.jpg"
            name = "glasses"
            def = "the glasses"
            input += "glasses"
            input += "the glasses"

            argument_low += { +"I've heard glasses make people look smarter. And lots of smart humans lived a long time ago" }
            argument_low += { +"People use glasses to read books. And books have been around for a long time" }
            argument_high += { +"I hear it's common to use glasses when working the computer. And they are pretty recent" }
            argument_high += { +"To get a prescription for glasses you need a phoropter or a refractor. These are modern machines" }

            initiative {
                robot {+"Do you think I would look good with glasses?"}
                user(Yes()) {
                    robot {
                        +"That's nice of you to say"
                        +Gestures.BigSmile
                    }
                }
                user(No()) {
                    robot {+"Me neither. Thank god my vision is high definition"}
                }
                user {
                    robot {+"One problem is that I don't have ears to attach them to"}
                }
            }
        }

        card {
            id = "gunpowder"
            truth = 950
            image = "gunpowder.jpg"
            name = "gunpowder"
            def = "the gunpowder"
            input += "gunpowder"
            input += "the gunpowder"
            input += "powder"

            argument_low += { +"Many really old movies I've seen have bad guys armed to their teeth with guns" }
            argument_low += { +"I have heard that the gunpowder was invented in ancient China" }
            argument_high += { +"I think that humans have used bow and arrow for a very long time before gun powder existed" }

            initiative {
                robot {+"Did you know that gun powder was invented in Sweden?"}
                user {
                    robot {
                        +"Just kidding. That was dynamite."
                        +Gestures.Wink
                    }
                }
            }

        }

        card {
            id = "fork"
            truth = 982
            image = "gaffeln.jpg"
            name = "Fork"

            def = "the fork"
            input += "fork"
            input += "the fork"

            argument_low += { +"I wonder how people fed themselves before there was forks" }
            argument_low += { +"Any stick with sharp prongs could be considered a fork. And sticks are pretty old" }
            argument_high += { +"I think forks are a newer invention than chopsticks" }

            initiative {
                robot {+"I know that some humans prefer chopsticks, or even hands over forks. What do you prefer?"}
                user {
                    robot {
                        +"Cool. I don't have a strong opinion. I don't like eating"
                        +Gestures.BigSmile
                    }
                }
            }

        }

        card {
            id = "printing"
            truth = 1437
            image = "tryckpressen.jpg"
            name = "Printing press"

            def = "the printing press"
            input += "printing"
            input += "printing press"
            input += "the printing press"
            input += "the press"

            argument_low += { +"there are some really old books, how were they printed if there was no printing press?" }
            argument_high += { +"some of my more bohemian human friends still prefer handwriting over printed texts" }

            initiative {
                robot {+"What was the name of the guy who invented the printing press?"}
                user(intent("Guthenberg")) {
                    robot {+"Yes, you are right, Johannes Guthenberg"}
                }
                user(DontKnow()) {
                    robot {+"I think his name was Johannes Guthenberg"}
                }
                user {
                    robot {+"I think his name was Johannes Guthenberg"}
                }
            }

        }

        card {
            id = "corkscrew"
            truth = 1681
            image = "korkskruven.jpg"
            name = "The Corkscrew"

            def = "the corkscrew"
            input += "corkscrew"
            input += "the corkscrew"

            argument_low += { +"It must have been hard to seal bottles before there were corkscrews. And bottles have been around for ages" }
            argument_low += { +"You can buy bottles of wine salvaged from old shipwrecks. They must have had corkscrews back then as well." }
            argument_high += { +"I think corkscrews came with wine, which I think is a fairly recent drink" }
        }

        card {
            id = "stamp"
            truth = 1840
            image = "frimarket.jpg"
            name = "The Stamp"

            def = "the stamp"
            input += "stamp"
            input += "the stamp"

            argument_low += { +"I wonder how people sent mail without having stamps" }
            argument_low += { +"I have never needed to use a stamp, but I hear they worked well long time ago" }
            argument_low += { +"I heard some people collect stamps. People usually collect old things" }
            argument_high += { +"I think people paid the messengers directly for a long time before stamps existed" }
            argument_high += { +"If stamps were old, they would not still be a valid form of transportation for so many packages around the world" }
        }

        card {
            id = "zipper"
            truth = 1891
            image = "blixtlaset.jpg"
            name = "The Zipper"

            def = "the zipper"
            input += "zipper"
            input += "the zipper"

            argument_low += { +"Pants have been around for ages, and many of them have zippers" }
            argument_low += { +"If zippers were a new invention, so would be pants. But they are not." }
            argument_high += { +"I think zippers look quite advanced. Not like a robot of course, but still" }
            argument_high += { +"I think zippers need modern technology to be produced." }
        }

        card {
            id = "toothbrush"
            truth = 1498
            image = "_tandborsten.jpg"
            name = "The Toothbrush"

            def = "the toothbrush"
            input += "toothbrush"
            input += "tooth"
            input += "the toothbrush"

            argument_low += { +"Clean teeth must have been a thing even before humans were around" }
            argument_low += { +"Archaeological finds have included toothbrushes." }
            argument_high += { +"The toothbrush was probably invented when white teeth started being a norm. That was recent, wasn't it?" }
            argument_high += { +"The toothbrush was not needed before the age of the selfie" }
        }

        card {
            id = "sudoku"
            truth = 1979
            image = "sudoku.jpg"
            name = "Sudoku"

            def = "sudoku"
            input += "sudoku"

            argument_low += { +"Sudoko must have been invented by ancient Japanese" }
            argument_high += { +"Sudoko went mainstream not so long ago I think" }
        }

        card {
            id = "helicopter"
            truth = 1825
            image = "helikoptern.jpg"
            name = "The Helicopter"

            def = "the helicopter"
            input += "the helicopter"
            input += "helicopter"

            argument_low += { +"Didn't Davinci draw a helicopter like a very long time ago?" }
            argument_high += { +"An advanced piece of machinery, them helicopters" }
            argument_high += { +"Helicopters are complex pieces of machinery" }
        }

        card {
            id = "airplane"
            truth = 1903
            image = "flygplanet.jpg"
            name = "The Airplane"

            def = "the airplane"
            input += "the airplane"
            input += "airplane"
            input += "plane"
            input += "aeroplane"
            input += "air plane"

            argument_low += { +"I hear that humans have wanted to fly since they first looked up in the sky" }
            argument_low += { +"Lift is generated by the wing shape. Nothing technologically advanced about that." }
            argument_high += { +"Airplanes need really strong engines to be fast enough to lift off" }
            argument_high += { +"If airplanes were old, who would have used boats to discover the world" }

            initiative {
                robot {+"Do you remember who were the first to fly an airplane?"}
                user(intent("Wright")) {
                    robot {+"Yes, you are right, it was the Wright brothers"}
                }
                user(DontKnow()) {
                    robot {+"I think it was the Wright brothers"}
                }
                user {
                    robot {+"I think it was the Wright brothers"}
                }
            }
        }

        card {
            id = "bicycle"
            truth = 1817
            image = "cykeln.jpg"
            name = "The Bicycle"

            def = "the bicycle"
            input += "the bicycle"
            input += "bicycle"
            input += "the bike"
            input += "bike"

            argument_low += { +"biking is faster than running and I think humans have always been in a hurry" }
            argument_low += { +"After the invention of the wheel, two wheels and a frame seems like not such a stretch" }
            argument_high += { +"it probably took a while to figure out that it's possible to balance on only two wheels" }
            argument_high += { +"New electric bike startups are popping up every day. It must be new if the young people are in to it." }

            initiative {
                robot {+"Do you know how to bike?"}
                user(Yes()) {
                    robot {+"Maybe you could teach me? However, I don't have any arms or legs, so I guess it might be hard."}
                }
                user(No()) {
                    robot {+"Me neither, I don't have any arms or legs, so I guess it might be hard to learn."}
                }
                user {
                    robot {+"I don't have any arms or legs, so I guess it might be hard to learn."}
                }
            }
        }
    }
}
