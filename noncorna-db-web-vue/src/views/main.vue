<template>
  <div>
        <div>
          <main-header />
        </div>
        <div>
          <main-content />
        </div>
        <div>
          <main-footer />
        </div>

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
