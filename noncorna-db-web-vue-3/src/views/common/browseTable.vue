<template>
  <div>

    <el-container>
      <el-aside width="220px;max-height:500px;overflow-y:auto;">
        <el-input
          placeholder="keyword"
          v-model="filterText">
        </el-input>
        <el-tree
          class="filter-tree"
          :data="tree"
          accordion
          @node-click="handleNodeClick"
          default-expand-all
          :filter-node-method="filterNode"
          ref="tree">
        </el-tree>
      </el-aside>
      <el-main>
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
          <!--<el-table-column
            prop="databaseId"
            header-align="center"
            align="center"
            label="database_id">
          </el-table-column>-->
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
      </el-main>
    </el-container>

  </div>
</template>

<script>
export default {
  components: {

  },
  created () {
    this.getTree()
  },
  props: {
    catagory: {
      type: String,
      required: true
    }
  },
  watch: {
    filterText (val) {
      this.$refs.tree.filter(val)
    }
  },

  data () {
    return {
      dataForm: {
        'cancer': '',
        'gen': '',
        'immuneCell': '',
        'immunePathway': '',
        'status': 0
      },
      filterText: '',
      pageIndex: 1,
      pageSize: 10,
      dataList: [],
      totalPage: 0,
      dataListLoading: false,
      tree: []

    }
  },
  methods: {
    handleNodeClick (node) {
      console.log(node)
      if (node.children) {
        this.dataList = []
      } else {
        this.dataForm[node.category] = node.label
        this.getDataList()
      }
    },

    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/rna/rnainfo/web/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'cancer': this.dataForm.cancer,
          'gen': this.dataForm.gen,
          'immuneCell': this.dataForm.immuneCell,
          'immunePathway': this.dataForm.immunePathway,
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
    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    getTree () {
      this.$http({
        url: this.$http.adornUrl('/rna/rnainfo/web/infoTree/' + this.catagory),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log(data.tree)
          this.tree = data.tree
        } else {
          this.$message.error(data.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
  /deep/ .el-main{
    padding: 0 0 0 20px !important;
  }
  /deep/ .el-pagination{
    margin-top: 20px;
  }
  /deep/ .el-form-item{
    margin-bottom: 0 !important;
  }
  /deep/ .el-form--label-left .el-form-item__label {
    font-weight: bold !important;
  }
  /*/deep/ .el-table--border th{
    background-color: #f0f9eb;
  }*/
</style>
