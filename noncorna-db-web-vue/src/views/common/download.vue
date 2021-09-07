<template>
  <div class="downMain">
    <!--  banner  -->
    <div class="downBan"></div>
    <!--  conter  -->
    <div class="downContain">
      <div>
        <el-container>
          <el-aside width="250px">
            <el-tabs v-model="activeName" tab-position="left"   @tab-click="getCategoryNum"  >
              <el-tab-pane label="Cancer Type" name="Cancer Type">
              </el-tab-pane>
              <el-tab-pane label="Gen Type" name="Gen Type">
              </el-tab-pane>
            </el-tabs>
          </el-aside>
          <el-main>
            <div>
              <el-input style="width: 330px"
                        size="medium"
                        :placeholder="activeName"
                        v-model="dataForm.value"
                        prefix-icon="el-icon-search">
              </el-input>
              <el-button type="primary"   @click="getCategoryNum" icon="el-icon-search">Search</el-button>
              <el-button type="warning" icon="el-icon-download" @click="download('0','')">Download All</el-button>
            </div>

            <el-table
              :data="tableData"
              v-loading="dataListLoading"
              style="width: 100%;margin-top: 20px">
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
                  <el-button type="text" size="small" @click="download(1 , scope.row.category)">excel</el-button>
                  <el-button type="text" size="small" @click="downloadCvsOrTxt(1 , scope.row.category , 'csv')">csv</el-button>
                  <el-button type="text" size="small" @click="downloadCvsOrTxt(1 , scope.row.category , 'txt')">txt</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-main>
        </el-container>
      </div>
    </div>




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
       let objectUrl = URL.createObjectURL(blob) // 创建URL
        const link = document.createElement('a')
        link.href = objectUrl
        link.download = value + '.xlsx' // 自定义文件名
        link.click() // 下载文件
        URL.revokeObjectURL(objectUrl); // 释放内存
      })
    },
    downloadCvsOrTxt (downType, value ,type) {
      this.$http({
        url: this.$http.adornUrl('/rna/rnainfo/web/downloadByCategory4cvsOrTxt'),
        method: 'get',
        params: this.$http.adornParams({
          'column': downType === 0 ? '' : this.getColumn(),
          'type': type,
          'value': value
        }),
        responseType: 'blob'
      }).then(({data}) => {
        
        let typeStr 
        if(type == 'csv'){
          typeStr = 'application/csv'
        }else{
          typeStr = 'text/plain'
        }

        let blob = new Blob([data], {
          type: typeStr      // 将会被放入到blob中的数组内容的MIME类型
        })
        let objectUrl = URL.createObjectURL(blob) // 创建URL
        const link = document.createElement('a')
        link.href = objectUrl
        link.download = value + '.' + type // 自定义文件名
        link.click() // 下载文件
        URL.revokeObjectURL(objectUrl); // 释放内存
      })
    }

  }
}
</script>

<style scoped>
  .downMain{
    width: 100%;
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
  .downBan{
    width: 100%;
    height: 150px;
    background: url("~@/assets/img/nyban.jpg") center no-repeat;
  }
  .downContain{
    width: 1200px;
    margin: 30px auto 0 auto;
    overflow: hidden;
  }
  .down_left{
    width: 150px;
    float: left;
    margin-top: 20px;
  }
  .downLft_til{
    width: 150px;
    height: 80px;
    background-color: #114694 !important;
    color: #ffffff;
    line-height: 80px;
    text-align: center;
    font-weight: bold;
    font-size: 18px;
  }
  /*/deep/.downContain .el-tabs__item{
    padding: 0 10px 0 0 !important;
    background-color: #eead0a !important;
    border-bottom: 1px solid #b8b019;
    width: 172px;
    box-sizing: content-box;
    font-size: 18px;
    height: 55px;
    line-height: 55px;
    color: #ffffff;
  }*/
  /deep/.downContain .el-tabs__item.is-active {
    font-weight: bold;
  }
  /deep/.el-tabs--left .el-tabs__active-bar.is-left{
    top: 1px !important;
  }
</style>
