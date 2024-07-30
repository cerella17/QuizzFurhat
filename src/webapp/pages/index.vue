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
</template>