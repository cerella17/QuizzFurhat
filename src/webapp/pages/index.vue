<script setup lang="ts">
const {gameData, currentQuestionData, questionCountDown, isGameRunning} = useFurhatData()

</script>

<template>
  <h1>Is Game Running: {{ isGameRunning }}</h1>

  <h2>Game Data</h2>
  <pre>{{ gameData }}</pre>

  <h1>Countdown: {{ questionCountDown }}</h1>
  <h2>Current Question Data</h2>
  <pre>{{ currentQuestionData }}</pre>


  <div class="flex bg-red-500  flex  items-center flex-col h-[90vh]">

<img src="/public/images.png" class="absolute w-20 right-0">
    <div
        v-if="isGameRunning"
        class="w-full bg-red-950 flex items-center justify-center h-full">
      <h1 v-if="isGameRunning" class="text-2xl">
        La partita sta iniziando...
      </h1>
      <h1>
        Fine Gioco
      </h1>
    </div>

    <div class="h-full flex justify-center items-center">
      <h1>
        Cambio Squadra
      </h1>
    </div>


  </div>
</template>