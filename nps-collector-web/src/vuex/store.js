import Vue from 'vue'
import Vuex from 'vuex'
import Vapi from './vapi'
import NpsSurvey from './nps'

Vue.use(Vuex)

const apiStore = Vapi;

export default new Vuex.Store({
    modules: {
        npsForm: NpsSurvey,
        api: apiStore,
    }
})
