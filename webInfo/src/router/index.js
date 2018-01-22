import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
const Login = resolve => require(['@/components/login/Login'], resolve)

const Main = resolve => require(['@/components/Main'], resolve)

const Room = resolve => require(['@/components/computer/Computers'], resolve)
const Group = resolve => require(['@/components/NoGroup'], resolve)

const User = resolve => require(['@/components/userList'], resolve)
// const User = resolve => require(['@/components/test'], resolve)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/',
      component: Main,
      children: [
        {
          path: '',
          name: 'main',
          component: Group
        },
        {
          path: '/room/:roomId',
          name: 'room',
          component: Room
        },
        {
          path: '/userList',
          name: 'userList',
          component: User
        }
      ]
    }
  ]
})
