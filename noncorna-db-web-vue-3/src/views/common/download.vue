<template>
  <div class="downMain">


    <el-container>
      <el-header><div class="downTil">Download</div></el-header>
       <el-divider content-position="right"></el-divider>
      <el-container>
        <el-aside width="200px">
          <el-tabs v-model="activeName" tab-position="left"   @tab-click="getCategoryNum"  >
            <el-tab-pane label="Cancer Type" name="Cancer Type">
            </el-tab-pane>
            <el-tab-pane label="Gen Type" name="Gen Type">
            </el-tab-pane>
          </el-tabs>
        </el-aside>
        <el-main>
          <el-input style="width: 330px"
            size="medium"
            :placeholder="activeName"
            v-model="dataForm.value"
            prefix-icon="el-icon-search">
          </el-input>

          <el-button type="primary"   @click="getCategoryNum" icon="el-icon-search">Search</el-button>
          &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;
          &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
          <el-button type="warning" icon="el-icon-download" @click="download('0','')">Download All</el-button>

          <br/><br/>
          <el-table
            :data="tableData"
            v-loading="dataListLoading"
            style="width: 100%">
            <el-table-column
              prop="category"
              :label="activeName"
              width="180">
            </el-table-column>
            <el-table-column
              prop="dataNum"
              label="Data Num"
              width="180">
            </el-table-column>
            <el-table-column
              label="Download">

              <template slot-scope="scope">
                <el-button type="text" size="small" @click="download(1 , scope.row.category)">download</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </el-container>



  </div>
</template>

<script>
export default {
  name: 'download',
  data () {
    return {
      dataListLoading: false,
      dataForm: {
        column: '',
        value: ''
      },
      activeName: 'Cancer Type',
      tableData: []
    }
  },
  activated () {
    this.getCategoryNum()
  },
  methods: {
    getColumn () {
      if (this.activeName === 'Cancer Type') {
        return 'cancer_type'
      } else {
        return 'gene_type'
      }
    },
    getCategoryNum () {
      this.dataListLoading = true
      this.dataForm.column = this.getColumn()
      this.$http({
        url: this.$http.adornUrl('/rna/rnainfo/web/getCateGroyNum'),
        method: 'get',
        params: this.$http.adornParams({
          'column': this.dataForm.column,
          'value': this.dataForm.value
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.tableData = data.list
        } else {
          this.tableData = []
        }
        this.dataListLoading = false
      })
    },

    download (downType, value) {
      this.$http({
        url: this.$http.adornUrl('/rna/rnainfo/web/downloadByCategory'),
        method: 'get',
        params: this.$http.adornParams({
          'column': downType === 0 ? '' : this.getColumn(),
          'value': value
        }),
        responseType: 'blob'
      }).then(({data}) => {
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel'      // 将会被放入到blob中的数组内容的MIME类型
        })
        let objectUrl = URL.createObjectURL(blob)  // 生成一个url
        window.location.href = objectUrl   // 浏览器打开这个url
      })
    }

  }
}
</script>

<style scoped>
  .downMain{
    width: 1000px;
    margin: 0 auto;
  }
  .downTil{
    font-weight: bold;
    font-size: 22px;
  }
  .downText{
    font-size: 18px;
    line-height: 32px;
  }
  .downLink{
    margin: 20px 0 0 0;
    padding: 0;
  }
  .downLink li{
    display: inline-block;
    width: 150px;
    margin-right: 10px;
  }
  .downLink li a{
    display: block;
  }
  .downLink li a img{
    display: block;
    width: 150px;
  }
</style>
