<template>
  <div class="footer" > CopyRight © Harbin Medical University, Harbin, China. Please don't hesitate to address comments, questions or suggestions regarding this website to: jcl6688@163.com.
  </div>
</template>

<script>
export default {
  data () {
    return {
      webconfig: {}

    }
  },
  created () {
    // this.getWebConfig()
  },
  methods: {
   /* getWebConfig () {
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
    } */
  }
}
</script>

<style scoped>

.footer{
  text-align:center;
  background-color: #114694;

  padding:23px 0;
  color:#ffffff;
  font-size:16px;
}

</style>
