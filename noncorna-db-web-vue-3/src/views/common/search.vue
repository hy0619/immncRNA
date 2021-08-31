<template>
  <div class="mod-home">
    <div class="searcher_main">
      <div class="wraper"  >
        <p class="serTil">Search For Keywords </p>
        <div class="serC">
          <el-input size = "large"
                    v-model="keywords"
                    placeholder="keywords"
            prefix-icon="el-icon-search">
          </el-input>
          <el-button type="primary" @click="search()">Search</el-button>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
export default {
  name: 'search',
  created () {
  },
  activated () {
  },
  methods: {
    search () {
      this.$router.push({path: '/search-result', query: { 'keyWords': this.keywords }})
    }
  },
  data () {
    return {
      keywords: ''
    }
  }
}
</script>

<style scoped>
  .searcher_main{
    min-height: 400px;
  }
  .wraper{
    width: 600px;
    padding-top: 60px;
    margin: 0 auto;
  }
  .serTil{
    text-align: center;
    font-size: 26px;
    font-weight: bold;
  }
  .serC{
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
