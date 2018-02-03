import { navigateStep } from '../../lib/common'
import { get } from 'lodash'

export const SET_NPS_SCORE = (state, score) => {
    state.npsSurvey = { ...state.npsSurvey, score }
}

export const SET_COMMENT = (state, comment) => {
    state.npsSurvey = { ...state.npsSurvey, comment }
}

export const SET_CONTACTABLE = (state, isContactable) => {
    state.npsSurvey = { ...state.npsSurvey, isContactable }
}

export const SET_ACTIVE_STEP = (state, step) => {
    state.npsSurvey = { ...state.npsSurvey, step }
}

export const NEXT_STEP = (state, routeParams) => {
    const npsSurveyStep = get(state, 'npsSurvey.step')

    if (npsSurveyStep < 1) {
        state.npsSurvey.step += 1
        navigateStep(state.npsSurvey.step, routeParams)
    }
}

export const PREV_STEP = (state, routeParams) => {
    const npsSurveyStep = get(state, 'npsSurvey.step')

    if (npsSurveyStep > 0) {
        state.npsSurvey.step -= 1
        navigateStep(state.npsSurvey.step, routeParams)
    }
}
