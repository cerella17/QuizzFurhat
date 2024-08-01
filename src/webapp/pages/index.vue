<script setup lang="ts">

const {gameData, currentQuestionData, questionCountDown, isGameRunning, isGameEnded} = useFurhatData();

// Reactive state for controlling score display
const showScore = ref(false);

// Watch for changes in isGameEnded to trigger the timeout
watch(isGameEnded, (newVal) => {
  if (newVal) {
    showScore.value = false;
    setTimeout(() => {
      showScore.value = true;
    }, 2000);
  }
});


</script>
<!--
    cambio squadre da rivedere
 -->
<template>
  <div class="flex items-center flex-col  h-screen bg-amber-500 ">
    <img src="/images.png" class="absolute w-20 right-0">

    <!--Icona caricamento -->
    <div
        v-if="!isGameRunning && !isGameEnded"
        class="flex justify-center items-center h-full">
      <Icon name="svg-spinners:6-dots-rotate" size="50"/>
    </div>

    <!-- La partita sta per iniziare -->
    <div v-if="isGameRunning && !currentQuestionData?.team"
         class="w-full flex items-center justify-center h-full"

    >
      <h1 class="text-2xl">
        La partita sta iniziando...
      </h1>
    </div>
    <!-- Domanda, opzioni, tempo e score-->
    <div v-if="currentQuestionData?.team && !isGameEnded"
         :class="['flex items-center flex-col h-full justify-center gap-10 w-full', currentQuestionData?.team === 'BLUE' ? 'bg-blue-800' : 'bg-red-800']">
      <div
           class="flex flex-col justify-center items-center gap-4">
        <!--Score -->
        <div class="absolute top-1 left-1">
          <h1
           v-if="gameData?.red.leaderName != gameData?.blue.leaderName"
          class="text-3xl">
            {{gameData?.blue.leaderName}}:
            {{gameData?.blue.score}}
            {{gameData?.red.leaderName}}:
            {{gameData?.red.score}}
          </h1>
          <h1
          v-else
          class="text-3xl"
          >
            Squadra Blu: {{gameData?.blue.score}}
            Squadra Rossa: {{gameData?.red.score}}
          </h1>
        </div>
        <div class="countdown-container flex items-center gap-2">
          <i class="fas fa-stopwatch text-3xl"></i>
          <h1 class="countdown">{{ questionCountDown }}</h1>
        </div>
        <div class="question-container">
          <h1>{{ currentQuestionData?.question }}</h1>
          <div class="grid grid-cols-2 gap-4 mt-4">
            <div v-for="(option, index) in currentQuestionData.options"
                 :key="index"
                 :class="[
                   'option border p-2 text-center rounded-lg',
                   currentQuestionData?.result?.answer === option
                   ? (currentQuestionData?.result?.correct ? 'bg-green-500 animate-correct' : 'bg-red-500 animate-incorrect')
                   : ''
                 ]">
              {{ option }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--Partita terminata -->
    <div v-if="isGameEnded" class="h-full"
    >
      <div
          v-if="!showScore"
          class="w-full flex items-center justify-center h-full  ">
        <h1  class="text-2xl">
          La partita è terminata...
        </h1>
      </div>
      <div v-if="showScore && gameData" class="flex flex-col items-center justify-center mt-5 h-full">
        <h1 v-if="gameData?.red.score > gameData?.blue.score"
            class="text-2xl font-bold my-4 py-2 px-4 bg-red-100 text-red-600 rounded-lg">
          La squadra rossa ha vinto con uno score di {{ gameData?.red.score }}
        </h1>
        <h1 v-else-if="gameData?.blue.score > gameData?.red.score"
            class="text-2xl font-bold my-4 py-2 px-4 bg-blue-100 text-blue-600 rounded-lg">
          La squadra blu ha vinto con uno score di {{ gameData?.blue.score }}
        </h1>
        <h1 v-else class="text-2xl font-bold my-4 py-2 px-4 bg-gray-100 text-gray-700 rounded-lg">
          La partita è finita in parità con uno score di {{ gameData?.red.score }} a {{ gameData?.blue.score }}
        </h1>
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

.option:hover {
  background-color: rgba(0, 0, 0, 0.1);
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
