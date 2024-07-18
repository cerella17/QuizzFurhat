package furhatos.app.cardgame.locales.english.decks

import furhatos.app.cardgame.game.deck
import furhatos.app.cardgame.locales.english.englishLocale
import furhatos.app.cardgame.options
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val sugarInFoodEnglish = deck {

    imgFolder = "sugarInFood"
    name = "Sugar in foods"
    unitLabel = "grams" // Might not be needed, for example if you have YEARS as the unit. Because you don't say "1980 year" or similar, "1980" is enough

    input {
        singular += "food"

        min_def += "the least sugary food"
        min_def += "the least sugary"
        min_def += "the one with least sugar"

        max_def += "the most sugary food"
        max_def += "the most sugary"
        max_def += "the one with most sugar"
        max_def += "the sweetest"

        is_min_def += options("might be", "is", "could be", "may be") / min_def
        is_min_def += "may have the least sugar"
        is_min_def += "has the least sugar"
        is_min_def += "is not sugary at all"
        is_min_def += "is not sweet at all"

        is_max_def += options("might be", "is", "could be", "may be") / max_def
        is_max_def += "has the most sugar"

        is_less_than += "is less sugary than"
        is_less_than += "has less sugar than"
        is_less_than += "is not as sugary as"
        is_less_than += "is not as sweet as"
        is_less_than += "contains less sugar than"

        is_more_than += "is more sugary than"
        is_more_than += "has more sugar than"
        is_more_than += "is sweeter than"
        is_more_than += "contains more sugar than"
    }

    output {
        purpose = { +"In this game we are supposed to sort these foods by how much sugar they have per serving" }

        // Requires no variations
        singular = { +"food" }
        min_def = { +"the least sugary food" }
        max_def = { +"the most sugary food" }
        is_min = { +"has the least sugar" }
        is_max = { +"has the most sugar" }
        is_less_than = { +"has less sugar than" }
        is_more_than = { +"has more sugar than" }
    }

    // Cards. There can be unlimited cards here, but only 5 are selected for each game. A good amount to get variation is minimum 8. Please use the Lion card as a template. It is a good practise to have some cards that are quite extreme, i.e really slow or really fast.

    card {
        id = "blueberry_muffin"
        truth = 16
        image = "blueberry_muffin.jpg"
        name = "A blueberry muffin"

        def = "the blueberry muffin"
        input += "the blueberry muffin"
        input += "blueberry muffin"
        input += "the muffin"
        input += "muffin"

        argument_high += { +"Most desserts are sweet" }
        argument_high += { +"Pastries usually contain a fair amount of sugar" }
        argument_low += { +"blueberries contain a lot of anti-oxidants I've heard. That might mean they are healthy" }
        argument_low += { +"The muffin looks home made and home made pastries can be quite bland" }

        initiative {
            robot {+"Have you ever had a blueberry muffin?"}
            user(Yes()) {
                robot {+"me too, I really liked it"}
            }
            user(No()) {
                robot {+"Me neither, maybe we could have one together next time"}
            }
            user {
                robot {+"Yeah"}
            }
        }

    }

    card {
        id = "chocolate_milkshake"
        truth = 62
        image = "chocolate_milkshake.jpg" // The image of the card
        name = "A chocolate milkshake" // Displayed on card
        def = "the chocolate milkshake" // Definitive form ("bestämd form")

        // All different ways you can say this card in
        input += "chocolate milkshake"
        input += "the chocolate milkshake"
        input += "milkshake"
        input += "the milkshake"
        input += "shake"

        argument_low += { +"milkshakes is made of milk, which isn't very sweet" }
        argument_low += { +"Milkshake is a liquid and there is a limit to how much sugar you can dissolve in it" }
        argument_high += { +"chocolates tend to be sweet, and this milkshake looks chocolatey" }
        argument_high += { +"I think chocolate is the key word in chocolate milkshake" }
    }

    card {
        id = "fruited_yogurt"
        truth = 43
        image = "fruited_yogurt.jpg"
        name = "A fruited yogurt"
        def = "the fruited yogurt"
        input += "fruited yogurt"
        input += "yogurt"
        input += "the fruited yogurt"
        input += "the yogurt"

        argument_low += { +"I've heard healthy breakfasts are important for you humans. And this is a breakfast thing." }
        argument_low += { +"Fruits contain some sugar, but there usually are very little of them in the yogurt." }
        argument_high += { +"My colleague told me fruited yogurt made her fat, and sugar is a main reason for obesity." }
        argument_high += { +"I think people underestimate the sugar content in fruit." }
    }

    card {
        id = "carrot_juice"
        truth = 9
        image = "carrot_juice.jpg"
        name = "a glass of carrot juice"
        def = "the carrot juice"
        input += "carrot juice"
        input += "juice"
        input += "the carrot juice"
        input += "the juice"

        argument_low += { +"carrot is a vegetable, so juice from it must be healthy" }
        argument_low += { +"Do carrots even contain any sugar?" }
        argument_low += { +"Carrots are often components in savory meals" }
    }

    card {
        id = "candy_bar"
        truth = 36
        image = "candy_bar.jpg"
        name = "A candy bar"
        def = "the candy bar"
        input += "the candy bar"
        input += "candy bar"
        input += "chocolate bar"
        input += "the chocolate bar"
        input += "chocolate candy bar"
        input += "the chocolate candy bar"
        input += "snickers"

        argument_low += { +"Bars are labeled as healthy in my gym. That must mean they are healthy" }
        argument_high += { +"Candy bars feel stereotypically sweet. I'm guessing they are practically fancy sugar" }
        argument_high += { +"Candy bars are advertised to give you a lot of energy" }
    }

    card {
        id = "nutella"
        truth = 56
        image = "nutella.jpg"
        name = "100 grams nutella"
        def = "the nutella"
        input += "nutella"
        input += "the nutella"
        input += "the nutella jam"
        input += "100 grams nutella"

        argument_low += { +"Nutella is from Italy I've heard, and most italians I meet seem healthy" }
        argument_high += {
            +"There must be a reason why most kids that I meet love Nutella"
            +Gestures.Thoughtful
        }
        argument_high += { +"Nutella is basically liquified sugar" }
    }
    card {
        id = "coke"
        truth = 35
        image = "coke.jpg"
        name = "a can of coke"
        def = "the coke"
        input += "coke"
        input += "the coke"
        input += "can of coke"
        input += "the can of coke"
        input += "cola"
        input += "the cola"
        input += "coca cola"

        argument_low += { +"Coke is just water with a lot of bubbles, is it not?" }
        argument_low += { +"Coca cola could not be that popular if it contained a lot of sugar" }
        argument_high += {
            +"I saw this documentary once about sugars. Coke was framed as the main villain"
            +Gestures.Surprise
        }
        argument_high += { +"After removing cocaine, they needed to add a lot of sugar to compensate" }
    }
    card {
        id = "ginger_shot"
        truth = 1
        image = "ginger_shot.jpg"
        name = "a small ginger shot"
        def = "the small ginger shot"
        input += "ginger shot"
        input += "the ginger shot"
        input += "ginger shots"
        input += "shot glass"

        argument_low += { +"Ginger shots are very popular in this office and people here don't seem too overweight" }
        argument_low += {
            +"Ginger shots is a personal favorite. And look at me, pretty fit right?"
            +Gestures.Wink
        }
    }
}