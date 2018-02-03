import Vue from 'vue'
import Router from 'vue-router'
import FeedbackForm from '../components/NpsSurvey/FeedbackForm/FeedbackForm.vue'
import NpsScoreForm from '../components/NpsSurvey/ScoreForm/NpsScoreForm.vue'
import NpsSurvey from '../components/NpsSurvey/NpsSurvey.vue'
import ThankYouPage from '../components/ThankYouPage/ThankYouPage.vue'
import SaWelcomePage from '../components/Common/SaWelcome.vue'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'Welcome!',
            component: SaWelcomePage
        },
        {
            path: '/thankyou',
            name: 'ThankYouPage',
            component: ThankYouPage
        },
        {
            path: '/survey/:customerReference',
            name: 'NpsSurvey',
            component: NpsSurvey,
            children: [
                {
                    path: '',
                    component: NpsScoreForm
                },
                {
                    path: 'feedback',
                    component: FeedbackForm
                }
            ]
        }
    ]
})
