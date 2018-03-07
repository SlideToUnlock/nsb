<template>
  <div>
    <div>
      <!-- 全部关机 -->
      <Button type="primary" @click="killAll()">全部关机</Button>
    </div>
    <Row :gutter="16">
      <Col span="4" v-for="item in computers" :key="item.id">
      <br>
      <Card>
        <p slot="title">{{item.ip}}</p>
        <i-Switch size="large" @on-change="change" :value="item.isBoot">
          <span slot="open">开机</span>
          <span slot="close">关机</span>
        </i-Switch>
        <p style="float: right" @click="process(item.ip,true)">
          <Icon type="ios-information" size="25" color="#5cadff"></Icon>
        </p>
      </Card>
      </Col>
    </Row>
    <Modal
      v-model="modal1"
      title="电脑进程信息"
      @on-ok="ok"
      @on-cancel="cancel">
      <Row>
        <Col span="20" offset="4" v-for="item in processList">
        <p style="float: left">
          {{item}}
        </p>
        <i-button type="error" v-if="killState" style="float: right" @click="killProcess(item)">关闭此进程</i-button>
        </Col>
        </br>
      </Row>
    </Modal>
    <BackTop></BackTop>
  </div>

</template>

<script>
  export default {
    data () {
      return {
        roomId: 0,
        computers: [],
        modal1: false,
        processList: [],
        title: '',
        mac: '',
        killState: false
      }
    },
    mounted () {
      // 获得机房ID
      this.roomId = this.$route.params.roomId
      this._getComputers()
    },
    methods: {
      _getComputers () {
        this.$http('/get_com_info/get_info').then(res => {
          this.computers = res.data.data.list
        })
      },
      ok () {
        this.$Message.info('Clicked ok')
      },
      cancel () {
        this.$Message.info('Clicked cancel')
      },
      // 获取进程
      process (ip, type) {
        this.modal1 = type
        this.title = ip + '电脑进程信息'
        // 获取当前ip的进程信息地址
        for (let item in this.computers) {
          let itemIp = this.computers[item].ip
          if (ip === itemIp) {
            // 判断当前电脑是否开机
            let boot = this.computers[item].isBoot
            if (boot === 'true') {
              this.processList = this.computers[item].processList
              this.killState = true
              this.mac = this.computers[item].mac
            } else {
              this.processList = ['当前电脑没有开机或者没有相关的运行进程']
            }
          }
        }
      },
      // 关闭进程
      killProcess (process) {
        // api/command/shutdown_process
        console.log(process)
        this.$http.post('/command/shutdown_process', {
          mac: this.mac,
          process: process
        }).then(res => {
          this.$Message.info(res.data.msg)
        })
      },
      // 关机
      change (status) {
        // false: 关机
        // true: 开机
        if (!status) {
          this.$http.get('/command/shutdown', {
            params: {'mac': this.mac}
          }).then(res => {
            this.$Message.info(res.data.msg)
          })
        }
      },
      // 关闭整个机房的电脑
      killAll () {
        this.$http.get('/command/shutdown', {
          params: {'roomId': this.roomId}
        }).then(res => {
          this.$Message.info(res.data.msg)
        })
      }
    }
  }
</script>
