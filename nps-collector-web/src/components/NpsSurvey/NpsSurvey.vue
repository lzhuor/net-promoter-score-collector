<template>
    <div>
        <pacman-loader
            :loading="pending"
            color="rgb(93, 197, 150)"/>
        <sa-route-view v-if="!pending"/>
    </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import { get } from 'lodash'
import PacmanLoader from 'vue-spinner/src/PacmanLoader.vue'
import SaRouteView from '../Common/SaRouteView.vue'

export default {
    components: {
        SaRouteView,
        PacmanLoader
    },

    computed: mapState({
        customerReference: state =>  get(state, 'route.params.customerReference'),
        pending: state => state.api.pending.npsAnswer,
    }),

    created() {
        this.getNpsQuestion({ params: { customerReference: this.customerReference } })
    },

    methods: mapActions([
        'getNpsQuestion',
    ]),}
</script>

<style>
    .question {
        padding-left: 1rem;
        padding-right: 1rem;
        margin-bottom: 3rem;
    }

    .step-control {
        background-color: #edeff1;
        text-align: center;
        border: none;
    }

    .step-control button {
        min-width: 10rem;
        cursor: pointer;
        padding: 0.8rem 3rem;
    }

    .btn.back {
        border-color: #cfd7e0;
        color: #233a54;
        background-color: white;
    }

    .btn.back:hover {
        color: #fff;
        background-color: #6c757d;
        border-color: #666e76;
        box-shadow: 0 0 0 0.2rem rgba(134, 142, 150, 0.5);
    }
</style>
