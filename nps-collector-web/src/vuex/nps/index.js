import * as actions from './actions'
import * as mutations from './mutations'
import * as getters from './getters'

/**
 * Initial State
 * @type {{}}
 */
const state = {
    npsSurvey: { step: 0 },
    npsOptions: Array.apply(null, { length: 11 }).map(Number.call, Number)
}

export default {
    state,
    mutations,
    actions,
    getters
}
