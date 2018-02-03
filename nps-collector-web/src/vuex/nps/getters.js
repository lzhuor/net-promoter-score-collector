import { get, isNil, isNumber, isNaN } from 'lodash'

export const npsScoreGetter = state => state.npsSurvey.score

export const npsCommentGetter = state => state.npsSurvey.comment

export const npsIsContactableGetter = state => state.npsSurvey.isContactable

export const npsOptionsGetter = state => state.npsOptions

export const npsSurveyActiveStepGetter = state => state.npsSurvey.step

export const isNpsActiveFormValidGetter = (state) => {
    const step = state.npsSurvey.step
    switch (step) {
    case 0:
        return isNumber(state.npsSurvey.score) && !isNaN(state.npsSurvey.score)
    case 1:
        return !isNil(state.npsSurvey.isContactable) && isNumber(state.npsSurvey.score)
    }
}

export const npsFeedbackFormContentGetter = (state) => {
    if (state.npsSurvey.score >= 9) {
        return {
            message: 'Cool!',
            description: 'We\'re glad to hear you\'re happy with your experience with StashAway so far! We\'d love to hear what you like as well as points of potential improvement.',
            question: 'Is there anything you really like, a feature you want, or a possible area of improvement?',
        }
    }

    return {
        message: 'Thanks for your input!',
        description: 'Whether it\'s a particular feature you want, or an overall gripe, ' +
        'we\'d like to know how we can improve your investment experience. After all, that\'s what we\'re here to do.',
        question: 'What can we do better?',
    }

}
