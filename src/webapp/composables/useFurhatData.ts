import type {IGameData, IQuestionData} from "~/types";

export default function useFurhatData() {
    const furhat = useFurhat()

    // undefined if no game is running
    const gameData = ref<IGameData>()
    // undefined if no question is being asked
    const currentQuestionData = ref<IQuestionData>()
    // is game running
    const isGameRunning = ref(false)
    // is game ended
    const isGameEnded = ref(false)

    const questionCountDown = ref(0)
    const intervaller = useIntervalFn(() => {
        questionCountDown.value--
        if (questionCountDown.value <= 0) {
            intervaller.pause()
        }
    }, 1000, {immediate: false})

    furhat.subscribe("NewGameEvent", (event) => {
        console.log(event.event_name, event)
        resetGame()
        isGameRunning.value = true
        gameData.value = {
            round: 1,
            maxRounds: event.maxRounds,
            red: {
                score: 0,
                leaderName: event.redLeaderName
            },
            blue: {
                score: 0,
                leaderName: event.blueLeaderName
            }
        }
    })
    furhat.subscribe("SyncInformationEvent", (event) => {
        console.log(event.event_name, event)
        if (!gameData.value) return
        gameData.value.round = event.round
        gameData.value.red.score = event.redScore
        gameData.value.blue.score = event.blueScore
    })
    furhat.subscribe("AskQuestionEvent", (event) => {
        console.log(event.event_name, event)
        currentQuestionData.value = {
            question: event.question,
            options: event.options,
            team: event.team,
            timeInMs: event.timeInMs
        }
        // start the countdown
        questionCountDown.value = event.timeInMs / 1000
        intervaller.resume()
    })
    furhat.subscribe("QuestionAnswerEvent", (event) => {
        console.log(event.event_name, event)
        if (!currentQuestionData.value) return
        currentQuestionData.value.result = {
            answer: event.answer,
            correct: event.correct,
            correctAnswer: event.correctAnswer
        }
        // stop the countdown
        intervaller.pause()
    })
    furhat.subscribe("EndGameEvent", (event) => {
        console.log(event.event_name, event)
        if (!gameData.value) return
        gameData.value.red.score = event.redScore
        gameData.value.blue.score = event.blueScore
        isGameRunning.value = false
        isGameEnded.value = true
    })

    function resetGame() {
        gameData.value = undefined
        currentQuestionData.value = undefined
        isGameRunning.value = false
        isGameEnded.value = false
        // stop the countdown
        intervaller.pause()
        questionCountDown.value = 0
    }

    return {
        gameData,
        currentQuestionData,
        questionCountDown,
        isGameRunning,
        isGameEnded,
    }
}