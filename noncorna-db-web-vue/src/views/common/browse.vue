<template>
  <div>
    <!--  banner  -->
    <div class="downBan"></div>
    <div class="browseCnt">
      <el-tabs v-model="activeName"  @tab-click="handleClick" type="border-card">
        <el-tab-pane label="Tissue Origin" name="tissueOrigin">
          <browse-table catagory="tissueOrigin" ></browse-table>
        </el-tab-pane>
          <el-tab-pane label="Pub Time" name="pubTime">
          <browse-table catagory="pubTime" ></browse-table>
        </el-tab-pane>
        <el-tab-pane label="Cancer" name="cancer">
          <browse-table catagory="cancer" ></browse-table>
        </el-tab-pane>
        <el-tab-pane label="Gene" name="gen">
          <browse-table catagory="gen"></browse-table>
        </el-tab-pane>
        <!-- <el-tab-pane label="Immune Cell" name="immuneCell">
          <browse-table catagory="immuneCell"></browse-table>
        </el-tab-pane> -->
        <el-tab-pane label="Suvival" name="suvival">
          <browse-table  catagory="suvival"></browse-table>
        </el-tab-pane>
       
      </el-tabs>
    </div>

  </div>
</template>x

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
  width: 1500px;
  margin: 25px auto 0 auto;
}
</style>
