import Vue from 'vue'
import App from './App'
import router from './router'
import iView from 'iview'
import axios from 'axios'
import Cookies from 'js-cookie'
import 'iview/dist/styles/iview.css'

Vue.use(iView)
Vue.config.productionTip = false
Vue.config.devtools = true

Vue.prototype.$http = axios.create({
  baseURL: 'http://localhost:8080/',
  headers: {'Authorization': 'Bearer ' + localStorage.getItem('jwt-token')}
})

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()
  if (!Cookies.get('user') && to.name !== 'login') {  // 判断是否登录
    next({
      path: '/login'
    })
  }
  next()
  iView.LoadingBar.finish()
})
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
