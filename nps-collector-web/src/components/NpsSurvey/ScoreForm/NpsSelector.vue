<template>
    <table class="nps-selector-wrapper">
        <nps-description/>

        <tbody>
            <tr>
                <td
                    :key="npsOption"
                    class="nps-option-cell"
                    v-for="npsOption in npsOptions">
                    <div
                        class="square-box"
                        align="center">

                        <div
                            :class="{
                                selected: activeScore === npsOption,
                                'face-sad': npsOption <= 6,
                                'face-neutral': npsOption > 6 && npsOption < 9,
                                'face-happy': npsOption >= 9
                            }"
                            @click="() => setNpsScore(npsOption)"
                            class="nps-option-content">

                            <img
                                v-if="npsOption <= 6"
                                src="../../../assets/face_sad.svg">

                            <img
                                v-if="npsOption > 6 && npsOption < 9"
                                src="../../../assets/face_neutral.svg">

                            <img
                                v-if="npsOption >= 9"
                                src="../../../assets/face_happy.svg">
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td
                    :key="npsOption"
                    class="nps-option-cell"
                    v-for="npsOption in npsOptions">

                    <div style="width: 90%; padding-top: 0.5rem">
                        <p class="text-center">{{ npsOption }}</p>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>

</template>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import NpsDescription from './NpsDescription.vue'

  export default {
      components: {
          NpsDescription
      },

      computed: mapGetters({
          activeScore: 'npsScoreGetter',
          npsOptions: 'npsOptionsGetter'
      }),

      methods: mapActions([
          'setNpsScore'
      ])
  }
</script>

<style>
    .nps-selector-wrapper {
        min-width: 900px;
    }

    .nps-option-cell {
        width:9%;
        min-width:25px
    }

    .square-box {
        position: relative;
        width:90%;
        display: block;
        text-align:center
    }

    .square-box:after {
        content: "";
        display: block;
        padding-bottom: 100%;
    }

    .nps-option-content {
        cursor: pointer;
        left: 0;
        position: absolute;
        width: 100%;
        height: 100%;
        border:1px solid #ccc;
        padding: 15px;
        border-radius:3px;
        text-decoration: none;
        min-width:25px;
        color: #333332;
    }

    .nps-option-content img {
        width: 100%;
    }

    .nps-option-content.face-sad.selected {
        background: red;
    }

    .nps-option-content.face-sad.selected img {
      filter: brightness(0) invert(1);
    }

    .nps-option-content.face-neutral.selected {
        background: #dd9b04;
    }

    .nps-option-content.face-neutral.selected img {
        filter: brightness(0) invert(1);
    }

    .nps-option-content.face-happy.selected {
        background: #549c05;
    }

    .nps-option-content.face-happy.selected img {
        filter: brightness(0) invert(1);
    }
</style>
