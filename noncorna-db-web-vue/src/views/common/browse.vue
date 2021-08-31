<template>
  <div>
    <!--  banner  -->
    <div class="downBan"></div>
    <div class="browseCnt">
      <el-tabs v-model="activeName"  @tab-click="handleClick" type="border-card">
        <el-tab-pane label="CANCER" name="cancer">
          <browse-table catagory="cancer" ></browse-table>
        </el-tab-pane>
        <el-tab-pane label="GEN" name="gen">
          <browse-table catagory="gen"></browse-table>
        </el-tab-pane>
        <el-tab-pane label="IMMUNE CELL" name="immuneCell">
          <browse-table catagory="immuneCell"></browse-table>
        </el-tab-pane>
        <el-tab-pane label="IMMUNE PATHWAY" name="immunePathway">
          <browse-table v-loading ="loading" catagory="immunePathway"></browse-table>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>
import BrowseTable from './browseTable'
export default {
  name: 'browse',
  components: {
    BrowseTable
  },
  data () {
    return {
      activeName: 'cancer'
    }
  },
  methods: {
    handleClick () {

    }
  }
}
</script>

<style scoped>
.downBan{
  width: 100%;
  height: 150px;
  background: url("~@/assets/img/nyban.jpg") center no-repeat;
}
.browseCnt{
  width: 1200px;
  margin: 25px auto 0 auto;
}
</style>
