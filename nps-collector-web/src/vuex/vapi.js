import Vapi from 'vuex-rest-api'
import { get } from 'lodash'
import { PRECONDITION_FAILED } from 'http-status'
import { checkIsQuestionAnswered, navigateStep, navigateToThankYou } from '../lib/common'
import {NPS_FEEDBACK} from "../constants/npsSteps";

const env = process.env.NODE_ENV || 'development';

export default new Vapi({
    baseURL: env === 'development' ? 'http://localhost:8000/api/v1/nps-questions' : '/api/v1/nps-questions',
    state: {
        npsAnswer: {}
    }
})
    .get({
        action: 'getNpsQuestion',
        property: 'npsAnswer',
        queryParams: true,
        path: '/latest',
        onError: () => navigateToThankYou(),
        onSuccess: (state, payload) => checkIsQuestionAnswered(payload)
            ? navigateToThankYou() : state.npsAnswer = { ...payload.data },
    })
    .put({
        action: 'answerNpsScore',
        property: 'npsAnswer',
        path: ({ id }) => `/${id}`,
        onError: (state, error) => error.response.status === PRECONDITION_FAILED
            ? navigateStep(NPS_FEEDBACK, get(state.npsAnswer, 'customer.reference')) : console.log('error', state),
        onSuccess: (state, payload) => navigateStep(NPS_FEEDBACK, get(payload.data, 'customer.reference')),
    })
    .put({
        action: 'answerNpsAdditions',
        property: 'npsAnswer',
        path: ({ id }) => `/${id}/additions`,
        onError: (error) => console.log('error', error),
        onSuccess: () => navigateToThankYou(),
    })
    .getStore()
