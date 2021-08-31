<template>
  <p v-html="webconfig.helpContent"></p>
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

</style>
