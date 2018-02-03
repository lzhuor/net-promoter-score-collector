import { NPS_FEEDBACK, NPS_SELECT_SCORE } from '../constants/npsSteps';
import router from "../router";
import { get } from 'lodash';

export const navigateStep = (step, customerReference) => {
    const basePath = `/survey/${customerReference}`

    switch (step) {
        case NPS_SELECT_SCORE:
            router.push(`/survey/${customerReference}`)
            break
        case NPS_FEEDBACK:
            router.push(`${basePath}/feedback`)
            break
    }
}

export const checkIsQuestionAnswered = (payload) => get(payload, 'data.answered') === true

export const navigateToThankYou = () => router.push('/thankyou')
