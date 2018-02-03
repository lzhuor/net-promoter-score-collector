<template>
    <transition
        name="fade"
        mode="out-in"
        appear>
        <router-view/>
    </transition>
</template>

<style>
  .fade-enter-active, .fade-leave-active {
    transition: opacity 0.15s
  }

  .fade-enter, .fade-leave-active {
    opacity: 0
  }
</style>
