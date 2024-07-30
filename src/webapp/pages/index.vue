<script setup lang="ts">
import { ref } from 'vue';
const { gameData, currentQuestionData, questionCountDown, isGameRunning } = useFurhatData();

// Stato per la gestione dell'animazione e del colore di sfondo
const isSwitchingTeams = ref(false);

function startSwitchTeams() {
  isSwitchingTeams.value = true;
  setTimeout(() => {
    isSwitchingTeams.value = false;
  }, 2000); // Durata dell'animazione in millisecondi
}

// Funzione per cambiare squadra (esempio di chiamata per dimostrare l'animazione)
function changeTeam() {
  startSwitchTeams();
}
</script>

<template>
  <h1>Is Game Running: {{ isGameRunning }}</h1>

  <h2>Game Data</h2>
  <pre>{{ gameData }}</pre>

  <h1>Countdown: {{ questionCountDown }}</h1>
  <h2>Current Question Data</h2>
  <pre>{{ currentQuestionData }}</pre>

  <div class="flex items-center flex-col h-[90vh]">
    <img src="/public/images.png" class="absolute w-20 right-0">

    <div
        v-if="isGameRunning && !currentQuestionData?.team"
        class="w-full flex items-center justify-center h-full">
      <h1 v-if="isGameRunning" class="text-2xl">
        La partita sta iniziando...
      </h1>
    </div>

    <div
        v-if="currentQuestionData?.team"
        :class="['flex items-center flex-col h-full justify-center gap-10 w-full', currentQuestionData?.team === 'BLUE' ? 'bg-blue' : 'bg-red']">
      <div v-if="!currentQuestionData.question">
        <h1 class="text-3xl">
          Cambio Squadra
        </h1>
        <Icon :class="{'rotate-icon': isSwitchingTeams}" name="tabler:switch-horizontal" class="size-20" />
      </div>

      <div
          v-if="currentQuestionData.question"
          class="flex flex-col justify-center items-center gap-4">
        <div class="countdown-container flex items-center gap-2">
          <i class="fas fa-stopwatch text-3xl"></i>
          <h1 class="countdown">{{ questionCountDown }}</h1>
        </div>
        <div class="question-container">
          <h1>{{currentQuestionData?.question}}</h1>
          <div class="grid grid-cols-2 gap-4 mt-4">
            <div
                v-for="(option, index) in currentQuestionData.options"
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
  </div>
</template>

<style scoped>
@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.rotate-icon {
  animation: rotate 2s linear infinite;
}

.bg-red {
  background-color: red;
}

.bg-blue {
  background-color: blue;
}

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

.bg-green-500 {
  background-color: #38a169;
}

.bg-red-500 {
  background-color: #e53e3e;
}

.animate-correct {
  animation: correct 0.5s ease-out;
}

.animate-incorrect {
  animation: incorrect 0.5s ease-out;
}

@keyframes correct {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

@keyframes incorrect {
  0% { transform: scale(1); }
  50% { transform: scale(0.9); }
  100% { transform: scale(1); }
}
</style>
