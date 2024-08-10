<script setup lang="ts">
import { vAutoAnimate } from "@formkit/auto-animate"

const { gameData, currentQuestionData, questionCountDown, isGameRunning, isGameEnded } =
  useFurhatData()

// Reactive state for controlling score display
const showScore = ref(false)

// Watch for changes in isGameEnded to trigger the timeout
watch(isGameEnded, (newVal) => {
  if (newVal) {
    showScore.value = false
    setTimeout(() => {
      showScore.value = true
    }, 2000)
  }
})

const haDettoUnaPresente = computed(
  () =>
    currentQuestionData.value?.result &&
    currentQuestionData.value?.options
      .map((x) => x.toLocaleLowerCase())
      .includes(currentQuestionData.value.result.answer.toLowerCase())
)
</script>

<template>
  <div class="flex items-center flex-col h-screen bg-amber-500" v-auto-animate>
    <img src="/images.png" class="absolute w-20 right-0" alt="img unisa" />

    <!--Icona caricamento -->
    <div v-if="!isGameRunning && !isGameEnded" class="flex justify-center items-center h-full">
      <Icon name="svg-spinners:6-dots-rotate" size="50" />
    </div>

    <!-- La partita sta per iniziare -->
    <div
      v-if="isGameRunning && !currentQuestionData?.team"
      class="w-full flex items-center justify-center h-full"
    >
      <h1 class="text-2xl">La partita sta iniziando...</h1>
    </div>

    <!--Saluta i Capo squadra -->
    <!-- Saluta i Capo squadra -->
    <div class="w-full flex flex-col items-center justify-center h-full space-y-4">
      <div class="bg-red-100 text-red-600 rounded-full px-6 py-3 text-2xl font-bold shadow-md">
        🎉 Ciao {{gameData?.red.leaderName}}! 🎉
      </div>
      <div class="bg-blue-100 text-blue-600 rounded-full px-6 py-3 text-2xl font-bold shadow-md">
        🎉 Ciao {{gameData?.blue.leaderName}}! 🎉
      </div>
      <div class="bg-green-100 text-green-600 rounded-lg px-8 py-4 text-xl font-semibold mt-4 shadow-lg text-center">
        🌟 Buon divertimento, {{gameData?.red.leaderName}} e {{gameData?.blue.leaderName}}! <br>Che vinca la squadra migliore! 🌟
      </div>
    </div>

    <!-- Domanda, opzioni, tempo e score-->
    <div
      v-if="currentQuestionData?.team && !isGameEnded"
      :class="[
        'flex items-center flex-col h-full justify-center gap-10 w-full',
        currentQuestionData?.team === 'BLUE' ? 'bg-blue-800' : 'bg-red-600',
      ]"
    >
      <div class="flex flex-col justify-center items-center gap-4">
        <!--Score -->
        <div
            v-if="gameData?.red.leaderName != gameData?.blue.leaderName"
            class="absolute bottom-1 justify-between left-1 flex w-full px-2">
          <h1  class="text-3xl">

            {{ gameData?.red.leaderName }}:
            {{ gameData?.red.score }}
          </h1>
          <h1 class="text-3xl"> {{ gameData?.blue.leaderName }}:
            {{ gameData?.blue.score }}</h1>
        </div>
        <div
            v-else
            class="absolute bottom-1 justify-between left-1">
          <h1  class="text-3xl">
            Squadra Rossa : {{ gameData?.red.score }}
          </h1>
          <h1>Squadra Blu: {{ gameData?.blue.score }} </h1>
        </div>

        <div class="countdown-container flex items-center gap-2">
          <i class="fas fa-stopwatch text-3xl"></i>
          <h1 class="countdown md:text-4xl">{{ questionCountDown }}</h1>
        </div>

        <div class="question-container md:text-4xl">
          <h1>{{ currentQuestionData?.question }}</h1>
          <div class="grid grid-cols-2 gap-4 mt-4">
            <div
              v-for="(option, index) in currentQuestionData.options"
              :key="index"
              class="option border p-2 text-center rounded-lg bg-transparent md:py-4 md:text-2xl"
              :class="[
                currentQuestionData.result?.correctAnswer.toLowerCase() === option.toLowerCase()
                  ? '!bg-green-500 animate-correct'
                  : (haDettoUnaPresente === false &&
                      currentQuestionData.result?.correct === false) ||
                    currentQuestionData.result?.answer.toLowerCase() === option.toLowerCase()
                  ? '!bg-red-500 animate-incorrect'
                  : '',
              ]"
            >
              {{ option }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--Partita terminata -->
    <div v-if="isGameEnded" class="h-full">
      <div v-if="!showScore" class="w-full flex items-center justify-center h-full">
        <h1 class="text-2xl">La partita è terminata...</h1>
      </div>
      <div
          v-if="showScore && gameData"
          class="flex flex-col items-center justify-center mt-5 h-full"
      >
        <div v-if="gameData?.red.score > gameData?.blue.score" class="text-center">
          <h1
              class="text-4xl font-extrabold my-4 py-2 px-4 bg-red-100 text-red-600 rounded-lg"
          >
            La squadra rossa ha vinto con uno score di {{ gameData?.red.score }}
          </h1>
          <p class="text-lg my-2 text-blue-700">
            La squadra blu ha ottenuto uno score di {{ gameData?.blue.score }}
          </p>
        </div>
        <div v-else-if="gameData?.blue.score > gameData?.red.score" class="text-center">
          <h1
              class="text-4xl font-extrabold my-4 py-2 px-4 bg-blue-100 text-blue-600 rounded-lg"
          >
            La squadra blu ha vinto con uno score di {{ gameData?.blue.score }}
          </h1>
          <p class="text-lg my-2 text-red-700">
            La squadra rossa ha ottenuto uno score di {{ gameData?.red.score }}
          </p>
        </div>
        <div v-else class="text-center">
          <h1
              class="text-2xl font-bold my-4 py-2 px-4 bg-gray-100 text-gray-700 rounded-lg"
          >
            La partita è finita in parità con uno score di {{ gameData?.red.score }} a
            {{ gameData?.blue.score }}
          </h1>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
.countdown-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.countdown {
  font-size: 2.5rem;
  font-weight: bold;
  color: #333;
}

.question-container {
  text-align: center;
}

.option {
  transition: background-color 0.3s, transform 0.3s;
}

.fa-stopwatch {
  color: #ff4500;
}

.animate-correct {
  animation: correct 0.5s ease-out;
}

.animate-incorrect {
  animation: incorrect 0.5s ease-out;
}

@keyframes correct {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes incorrect {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(0.9);
  }
  100% {
    transform: scale(1);
  }
}
</style>
