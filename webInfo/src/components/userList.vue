<template>
  <div>
    <!-- 搜索用户 -->
    <Input v-model="items.searchValue" ref="searchValue" icon="person" placeholder="输入用户" style="width: 200px"></Input>
    <!-- 添加新的用户 -->
    <Button type="primary" icon="person-add" @click="modal = true">添加新用户</Button>
    <Modal
      v-model="modal"
      title="添加新用户"
      @on-ok="addUser"
      @on-cancel="cancel">
      <Form :model="formRight" label-position="right" :label-width="100">
        <FormItem label="用户名：">
          <Input v-model="formRight.username"></Input>
        </FormItem>
        <FormItem label="实名：">
          <Input v-model="formRight.name"></Input>
        </FormItem>
        <FormItem label="密码：">
          <Input type="password" v-model="formRight.password"></Input>
        </FormItem>
        <FormItem label="角色：">
          <Select v-model="formRight.role">
            <Option value="1">管理员</Option>
            <Option value="0">普通用户</Option>
          </Select>
        </FormItem>
      </Form>
    </Modal>
    <br>
    <br>
    <Table border :columns="columns" :data="data"></Table>
    <br>
    <div style="float: right;">
      <Page :total="Number(allData.length)" :current="1" @on-change="changePage"></Page>
    </div>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        formRight: {
          username: '',
          name: '',
          password: '',
          role: ''
        },
        modal: false,
        items: {
          searchValue: ''
        },
        columns: [
          {
            title: '用户名',
            key: 'username',
            render: (h, params) => {
              return h('div', [
                h('Icon', {
                  props: {
                    type: 'person'
                  }
                }),
                h('strong', params.row.username)
              ])
            }
          },
          {
            title: '用户',
            key: 'name'
          },
          {
            title: '角色',
            key: 'role'
          },
          {
            title: '操作',
            key: 'action',
            width: 150,
            align: 'center',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.resetPwd(params)
                    }
                  }
                }, '重置密码'),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.remove(params)
                    }
                  }
                }, '删除')
              ])
            }
          }
        ],
        data: [],
        tempData: [], // 当前页码内容
        allData: []  // 所有数据
      }
    },
    mounted () {
      this.getUserList()
    },
    watch: {
      items: {
        handler: function (val, oldval) {
          console.log(this.$refs.searchValue.value)
        },
        deep: true
      }
    },
    methods: {
      getUserList () {
        this.$http.get('/user/get_users').then(res => {
          console.log(res.data.data)
          let resData = res.data.data
          let tempData = []
          for (let i = 0; i < resData.length; i++) {
            let item = {
              username: resData[i].username,
              name: resData[i].name,
              role: resData[i].role === 1 ? '管理员' : '普通用户'
            }
            tempData.push(item)
            if (tempData.length === 10) {
              this.allData.push(tempData)
              tempData = []
            }
            this.data.push(item)
          }
        })
      },
      changePage (event) {
        if (this.allData.length >= event) {
          this.data = this.allData[event - 1]
        }
        console.log('当前页码' + event)
        // 这里直接更改了模拟的数据，真实使用场景应该从服务端获取数据
      },
      resetPwd (params) {
        let username = params.row.username
        this.$http.get('/user/reset_password', {params: {username: username}}).then(res => {
          if (res.data.status === 0) {
            this.$Message.success(res.data.msg)
          } else {
            this.$Message.error(res.data.msg)
          }
        })
      },
      remove (params) {
        // 删除当前用户
        let username = params.row.username
        this.$http.get('/user/del_user', {params: {username: username}}).then(res => {
          this.data.splice(params.row.index, 1)
          this.$Message.success(res.data.msg)
          location.reload()
        })
      },
      addUser () {
        // 添加新用户
        this.$http.post('/user/addUser', {
          username: this.formRight.username,
          password: this.formRight.password,
          name: this.formRight.name,
          role: this.formRight.role
        }).then(res => {
          this.$Message.info(res.data.msg)
          location.reload()
        })
      },
      cancel () {
        this.$Message.info('取消添加')
      }
    }
  }
</script>
