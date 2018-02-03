<template>
    <div
        id="app"
        :class="{ 'disable-responsive': activeStep === 0 }">

        <b-container>
            <sa-header/>

            <div class="app-content">
                <sa-route-view/>
            </div>
        </b-container>
    </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import { mapGetters } from 'vuex'

import SaHeader from './components/Common/SaHeader.vue'
import SaRouteView from './components/Common/SaRouteView.vue'

export default {
    name: 'App',
    components: {
        SaHeader,
        SaRouteView
    },
    computed: {
        ...mapGetters({
            activeStep: 'npsSurveyActiveStepGetter'
        })
    },
}
</script>

<style>
#app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    margin-top: 60px;
    background-color: #edeff1;
}

html {
    background-color: #edeff1;
}

html .disable-responsive, .disable-responsive header {
    min-width: 960px;
}

.container {
    padding-top: 2rem;
}

.app-content {
    padding-top: 2rem;
}

.title {
    padding-top: 1rem;
    font-size: 1.5rem;
}

.v-spinner {
    width: 100px;
    margin: 30vh auto;
}
</style>
