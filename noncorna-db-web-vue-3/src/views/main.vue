<template>
  <div>
      <el-container>
        <div>
          <main-header />
        </div>
        <el-main>
          <main-content />
        </el-main>
        <el-footer>
          <main-footer />
        </el-footer>
      </el-container>


  </div>
</template>

<script>
  import MainHeader from './main-header'
  import MainFooter from './main-footer'
  import MainContent from './main-content'
  export default {
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainHeader,
      MainFooter,
      MainContent
    },
    computed: {
    },
    created () {
     // this.getUserInfo()
    },
    mounted () {
    },
    methods: {
    }
  }
</script>
<style>
</style>
