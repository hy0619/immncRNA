<template>
  <div class="helpCnt">
    <p v-html="webconfig.helpContent"></p>
  </div>

</template>

<script>
export default {
  data () {
    return {
      webconfig: {}
    }
  },
  activated () {
    this.getWebConfig()
  },
  methods: {
    getWebConfig () {
      this.$http({
        url: this.$http.adornUrl('/web/config/get'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.webconfig = data.config
        } else {
          this.$message.error(data.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.helpCnt{
  width: 1200px;
  margin: 0 auto;
}
</style>
