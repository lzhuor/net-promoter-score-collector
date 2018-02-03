import { get } from 'lodash';

export const setNpsScore = ({ commit }, score) => {
    commit('SET_NPS_SCORE', score)
}

export const setComment = ({ commit }, comment) => {
    commit('SET_COMMENT', comment)
}

export const setContactable = ({ commit }, isContactable) => {
    commit('SET_CONTACTABLE', isContactable)
}

export const setActiveStep = ({ commit }, step) => {
    commit('SET_ACTIVE_STEP', step)
}

export const nextStep = ({ commit, rootState }) => {
    commit('NEXT_STEP', get(rootState, 'route.params.customerReference'))
}

export const prevStep = ({ commit, rootState }) => {
    commit('PREV_STEP', get(rootState, 'route.params.customerReference'))
}

export const submitNpsScore = ({ dispatch, state, rootState }) => {
    const id = rootState.api.npsAnswer.id
    const npsScoreAnswer = {
        answer: state.npsSurvey.score,
    }

    dispatch('answerNpsScore', { params: { id }, data: npsScoreAnswer })
}

export const submitNpsAdditions = ({ dispatch, state, rootState }) => {
    const id = rootState.api.npsAnswer.id
    const npsAdditionsAnswer = {
        comment: state.npsSurvey.comment,
        contactable: state.npsSurvey.isContactable,
    }

    dispatch('answerNpsAdditions', { params: { id }, data: npsAdditionsAnswer })
}
