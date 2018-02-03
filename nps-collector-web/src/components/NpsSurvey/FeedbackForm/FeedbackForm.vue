<template>
    <div class="question">
        <b-row>
            <p class="title">{{ content.message }}</p>
        </b-row>
        <b-row>
            <p>{{ content.description }}</p>
        </b-row>

        <b-row>
            <p class="title">{{ content.question }}</p>
        </b-row>

        <b-row>
            <b-form-textarea
                :max-rows="8"
                :value="comment"
                :rows="6"
                @input="setComment"
                placeholder="Write your comment (optional)"
                style="margin-bottom: 1rem;"/>
        </b-row>

        <b-row>
            <b-form-group label="Would it be okay if our Customer Support Team reaches out to you to learn more about what you think?">
                <b-form-radio-group
                    @input="setContactable"
                    :checked="isContactable"
                    :options="isContactableOptions"
                    name="contactableRadioOptions"/>
            </b-form-group>
        </b-row>

        <b-row class="step-control">
            <b-button
                :disabled="!isValid"
                @click="submitNpsAdditions"
                size="lg"
                variant="success">
                Submit
            </b-button>
        </b-row>
    </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import { NPS_FEEDBACK } from "../../../constants/npsSteps";

export default {
    data () {
        return {
            isContactableOptions: [
                { text: 'Yes', value: true },
                { text: 'No', value: false },
            ],
        }
    },

    computed: mapGetters({
        activeStep: 'npsSurveyActiveStepGetter',
        isValid: 'isNpsActiveFormValidGetter',
        score: 'npsScoreGetter',
        content: 'npsFeedbackFormContentGetter',
        comment: 'npsCommentGetter',
        isContactable: 'npsIsContactableGetter',
    }),

    mounted () {
        this.setActiveStep(NPS_FEEDBACK);
    },

    methods: mapActions([
        'submitNpsAdditions',
        'setActiveStep',
        'setContactable',
        'setComment',
    ])
}
</script>

<style>
    button.btn.back {
        margin-right: 1rem;
    }
</style>
