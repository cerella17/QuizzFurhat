<script setup lang="ts">
const furhat = useFurhat()
furhat.subscribe("NewGameEvent", (event) => {
  console.log("NewGameEvent", event)
})
</script>

<template>
  {{ furhat }}
</template>