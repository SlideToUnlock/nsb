<template>
  <div>
    <div>
      <div style="border-bottom: 1px solid #e9e9e9;padding-bottom:6px;margin-bottom:6px;">
        分到
        <Select v-model="model" style="width:200px">
          <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
        <Button type="primary" @click="group()">确定</Button>
        <Input v-model="value4" icon="android-search" placeholder="输入IP查询" style="width: 200px;float: right"></Input>
      </div>
      <Table ref="selection" :columns="columns" :data="data"></Table>
    </div>
    <BackTop></BackTop>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        value4: '',
        computers: [],
        cityList: [
          {
            value: '1',
            label: '1 机房'
          },
          {
            value: '2',
            label: '2 机房'
          },
          {
            value: '3',
            label: '3 机房'
          },
          {
            value: '4',
            label: '4 机房'
          },
          {
            value: '5',
            label: '5 机房'
          },
          {
            value: '6',
            label: '6 机房'
          },
          {
            value: '7',
            label: '7 机房'
          }
        ],
        model: '',
        columns: [
          {
            type: 'selection',
            width: 60,
            align: 'center'
          },
          {
            title: 'MAC',
            key: 'MAC'
          },
          {
            title: 'IP',
            key: 'IP'
          },
          {
            title: '状态',
            key: 'Status',
            render: (h, params) => {
              return h('div', [
                h('i-switch', {
                  props: {
                    value: true
                  },
                  slot: {
                    open: '开机',
                    close: '关机'
                  }
                })
              ])
            }
          },
          {
            title: '操作',
            key: 'Edit',
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
                      this.show(params.index)
                    }
                  }
                }, '信息'),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.remove(params.index)
                    }
                  }
                }, '删除')
              ])
            }
          }
        ],
        data: [
          {
            MAC: '20-47-47-DA-7E-0C',
            IP: '10.70.26.86'
          },
          {
            MAC: '20-47-47-DA-7E-0C',
            IP: '10.70.26.86'
          },
          {
            MAC: '20-47-47-DA-7E-0C',
            IP: '10.70.26.86'
          },
          {
            MAC: '20-47-47-DA-7E-0C',
            IP: '10.70.26.86'
          }
        ]
      }
    },
    mounted () {
      this.noGroupList()
    },
    methods: {
      noGroupList () {
        console.log('sssssssssssss')
      },
      group () {
        let objData = JSON.stringify(this.$refs.selection.objData)
        let jsonObj = JSON.parse(objData)
        // 获得机房ID
        let roomID = this.model
        console.log(jsonObj)
        console.log(roomID)
      },
      show (index) {
        this.$Modal.info({
          title: 'Info',
          content: `Name：${this.data[index].MAC}<br>Age：${this.data[index].IP}<br>Address：${this.data[index].address}`
        })
      },
      remove (index) {
        this.data.splice(index, 1)
      }
    }
  }
</script>
