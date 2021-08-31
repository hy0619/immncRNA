<template>
    <div class="mod-home">
        <div class="resultHeader">
          <div class="resultCnt header_C">
            <div class="header_logo">
              <img :src="imageUrl"  style="width: 214px;" @click="gotoIndex()"></img>
              <div class="header_rg">SearchResult</div>
            </div>
            <div class="header_sercher">
              <div class="hdSer_main">
                <el-input size = "large"
                          v-model="keywords"
                          placeholder="keywords"
                          prefix-icon="el-icon-search"
                style="width: 300px">
                </el-input>
                <el-button type="primary" @click="getQuickSearchRes()" style="height: 40px">Search</el-button>
              </div>
            </div>
          </div>
        </div>
        <div class="resultCnt">
          <div class="crumbNav">
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item :to="{ path: '/search' }">Search</el-breadcrumb-item>
              <el-breadcrumb-item>detail</el-breadcrumb-item>
            </el-breadcrumb>
          </div>


          <div>
            <el-table
              :data="dataList"
              v-loading="dataListLoading"
              style="width: 100%;">
              <el-table-column type="expand">
                <template slot-scope="props">
                  <el-form label-position="left" class="demo-table-expand"   >
                    <el-form-item label="pmid:">
                      <span>{{ props.row.pmid }}</span>
                    </el-form-item>

                    <el-form-item label="title:">
                      <span>{{ props.row.title }}</span>
                    </el-form-item>

                    <el-form-item label="journal:">
                      <span>{{ props.row.journal }}</span>
                    </el-form-item>
                    <el-form-item label="pub_time:">
                      <span>{{ props.row.pubTime }}</span>
                    </el-form-item>
                    <el-form-item label="organism:">
                      <span>{{ props.row.organism }}</span>
                    </el-form-item>
                    <el-form-item label="tissue_origin:">
                      <span>{{ props.row.tissueOrigin }}</span>
                    </el-form-item>

                    <el-form-item label="database_id:">
                      <span>{{ props.row.databaseId }}</span>
                    </el-form-item>
                    <el-form-item label="gene_exp_pattern:">
                      <span>{{ props.row.geneExpPattern }}</span>
                    </el-form-item>
                    <el-form-item label="suvival:">
                      <span>{{ props.row.suvival }}</span>
                    </el-form-item>
                    <el-form-item label="major_targets:">
                      <span>{{ props.row.majorTargets }}</span>
                    </el-form-item>
                    <el-form-item label="target_gene:">
                      <span>{{ props.row.targetGene }}</span>
                    </el-form-item>
                    <el-form-item label="downstream_effect:">
                      <span>{{ props.row.downstreamEffect }}</span>
                    </el-form-item>
                    <el-form-item label="function_method:">
                      <span>{{ props.row.functionMethod }}</span>
                    </el-form-item>
                    <el-form-item label="immune_cell:">
                      <span>{{ props.row.immuneCell }}</span>
                    </el-form-item>
                    <el-form-item label="immune_pathway:">
                      <span>{{ props.row.immunePathway }}</span>
                    </el-form-item>
                    <el-form-item label="immune_activity:">
                      <span>{{ props.row.immuneActivity }}</span>
                    </el-form-item>
                    <el-form-item label="evidence_code:">
                      <span>{{ props.row.evidenceCode }}</span>
                    </el-form-item>

                    <el-form-item label="downstream_effect_evidence:">
                      <span>{{ props.row.downstreamEffectEvidence }}</span>
                    </el-form-item>
                    <el-form-item label="downstream_effect_method:">
                      <span>{{ props.row.downstreamEffectMethod }}</span>
                    </el-form-item>
                    <el-form-item label="function:">
                      <span>{{ props.row.function }}</span>
                    </el-form-item>
                    <el-form-item label="funtion_evidence:">
                      <span>{{ props.row.funtionEvidence }}</span>
                    </el-form-item>

                  </el-form>
                </template>
              </el-table-column>
              <el-table-column
                prop="geneId"
                header-align="center"
                align="center"
                label="gene id">
              </el-table-column>
              <el-table-column
                prop="cancerType"
                header-align="center"
                align="center"
                label="cancer type">
              </el-table-column>
              <el-table-column
                prop="cancerTypeAd"
                header-align="center"
                align="center"
                label="cancer type ad">
              </el-table-column>
              <el-table-column
                prop="geneSymbol"
                header-align="center"
                align="center"
                label="gen symbol">
              </el-table-column>
              <el-table-column
                prop="geneType"
                header-align="center"
                align="center"
                label="gene type">
              </el-table-column>

              <el-table-column
                prop="immuneCell"
                header-align="center"
                align="center"
                label="immune cell">
              </el-table-column>
              <el-table-column
                prop="immunePathway"
                header-align="center"
                align="center"
                label="immune pathway">
              </el-table-column>

            </el-table>
            <el-pagination
              @size-change="sizeChangeHandle"
              @current-change="currentChangeHandle"
              :current-page="pageIndex"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pageSize"
              :total="totalPage"
              layout="total, sizes, prev, pager, next, jumper">
            </el-pagination>
          </div>
        </div>
    </div>


</template>

<script>
export default {
  name: 'searchResult',
  data () {
    return {
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataList: [],
      dataListLoading: false,
      imageUrl: require('@/assets/img/logo.jpg'),
      keywords: ''
    }
  },
  created () {
    let keyWords = this.$route.params.keyWords || this.$route.query.keyWords
    this.keywords = keyWords
    this.getQuickSearchRes()
  },
  methods: {
    gotoIndex () {
      window.location = '/home'
    },
    getQuickSearchRes () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/rna/rnainfo/web/search'),
        method: 'get',
        params: this.$http.adornParams({
          'keyWords': this.keywords,
          'status': 0
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    }
  }
}
</script>

<style scoped>
  .mod-home{
    background-color: #ebeff4;
    min-height: 100%;
  }
  .resultHeader{
    background-color: #ffffff;
    -moz-box-shadow:0px 2px 5px #ccc;
    -webkit-box-shadow:0px 2px 5px #ccc;
    box-shadow:0px 2px 5px #ccc;
  }
  .header_C{
    overflow: hidden;
  }
  .resultCnt{
    width: 1200px;
    margin: 0 auto;
    height: 90px;
  }
  .header_logo{
    overflow: hidden;
    float: left;
    align-items: center;
    justify-content: center;
    display: flex;
    height: 90px;
  }
  .header_rg{
    height: 40px;
    line-height: 40px;
    font-size: 28px;
    border-left: 1px solid #909090;
    margin-left: 20px;
    padding-left: 20px;
    color: #686868;
  }
  .header_sercher{
    float: right;
    padding-top: 20px;
  }
  .hdSer_main{
    width: 400px;
  }
  .crumbNav{
    height: 40px;
  }
  .crumbNav .el-breadcrumb{
    height: 40px;
    line-height: 40px;
  }
  .el-pagination{
    text-align: right;
    margin-top: 20px;
  }
</style>
