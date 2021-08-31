<template>
  <div>
    <el-tabs v-model="activeName"  @tab-click="handleClick" >
      <el-tab-pane label="cancer" name="cancer">
        <browse-table catagory="cancer" ></browse-table>
      </el-tab-pane>
      <el-tab-pane label="gen" name="gen">
        <browse-table catagory="gen"></browse-table>
      </el-tab-pane>
      <el-tab-pane label="immune cell" name="immuneCell">
        <browse-table catagory="immuneCell"></browse-table>
      </el-tab-pane>
      <el-tab-pane label="immune pathway" name="immunePathway">
        <browse-table v-loading ="loading" catagory="immunePathway"></browse-table>
      </el-tab-pane>
    </el-tabs>
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

</style>
