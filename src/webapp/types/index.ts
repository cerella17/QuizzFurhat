export interface IGameData {
    round: number // Current round
    maxRounds: number // Total rounds
    red: { // Red team data
        score: number
        leaderName: string
    }
    blue: { // Blue team data
        score: number
        leaderName: string
    }
}

export interface IQuestionData {
    question: string // Question text
    options: string[] // Array of 4 options
    team: "RED" | "BLUE"
    timeInMs: number // Time in milliseconds to answer the question
    result?: {
        correct: boolean // True if user's answer is correct
        answer: string // User's answer
        correctAnswer: string // Correct answer
    }
}