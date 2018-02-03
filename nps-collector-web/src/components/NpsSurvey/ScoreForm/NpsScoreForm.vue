<template>
    <div class="question">
        <b-row>
            <p class="title">Hi, {{ firstName }}</p>
        </b-row>
        <b-row>
            <p>How you feel about your investing experience is very important to us. It's actually what we care about most! We'd appreciate a minute or two of your time for you to share what you really think of investing with StashAway.</p>
        </b-row>

        <b-row>
            <p class="title">How likely are you to recommend StashAway to a friend?</p>
        </b-row>

        <b-row>
            <nps-selector/>
        </b-row>

        <div class="step-control">
            <b-button
                @click="submitNpsScore"
                :disabled="!isValid"
                size="lg"
                variant="success">
                Submit
            </b-button>
        </div>
    </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from 'vuex'
import { get, isNumber, isNaN } from 'lodash'
import NpsSelector from './NpsSelector.vue'
import { NPS_SELECT_SCORE } from "../../../constants/npsSteps";

export default {
    components: {
        NpsSelector
    },

    computed: {
        ...mapGetters({
            activeStep: 'npsSurveyActiveStepGetter',
            isValid: 'isNpsActiveFormValidGetter',
        }),

        ...mapState({
            firstName: state => get(state, 'api.npsAnswer.customer.info.firstName'),
            npsScoreFromQuery: state => Number(get(state.route, 'query.score'))
        })
    },

    mounted () {
        this.setActiveStep(NPS_SELECT_SCORE);
        isNumber(this.npsScoreFromQuery) && !isNaN(this.npsScoreFromQuery) && this.setNpsScore(this.npsScoreFromQuery);
    },

    methods: mapActions([
        'nextStep',
        'setActiveStep',
        'setNpsScore',
        'submitNpsScore',
    ]),
}
</script>
