<template>
  <div>
    <Row :gutter="16">
      <Col span="3" v-for="item in computers" :key="item.id">
      <br>
      <Card>
        <p slot="title">computer- {{item}}</p>
        <p>Content of card</p>
      </Card>
      </Col>
    </Row>
    <BackTop></BackTop>
  </div>

</template>

<script>
  export default {
    data () {
      return {
        computers: []
      }
    },
    mounted () {
      // 获得机房ID
      console.log(this.$route.params.roomId)
      this._getComputers()
    },
    methods: {
      _getComputers () {
        let temp = []
        for (let i = 1; i < 100; i++) {
          temp.push(i)
        }
        this.computers = temp
      }
    }
  }
</script>
