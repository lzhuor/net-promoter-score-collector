// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import { sync } from 'vuex-router-sync'
import BootstrapVue from 'bootstrap-vue'
import App from './App'
import router from './router'
import store from './vuex/store'

Vue.config.productionTip = false
Vue.use(BootstrapVue)
sync(store, router)

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    components: { App },
    template: '<App/>'
})
