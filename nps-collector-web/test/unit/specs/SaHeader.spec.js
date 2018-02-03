import Vue from 'vue'
import SaHeader from '@/components/Common/SaHeader'

describe('SaHeader.vue', () => {
    it('should render correct contents', () => {
        const Constructor = Vue.extend(SaHeader)
        const vm = new Constructor().$mount()
        expect(vm.$el.querySelector('.logo').innerHTML)
            .toEqual('<img src="../../assets/logo.png" class="text-logo">')
    })
})
