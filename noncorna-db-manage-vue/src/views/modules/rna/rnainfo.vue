<template>
  <div class="mod-config">
    <el-form ref="dataForm" :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-row  :gutter="0">
        <el-col :span="3"  >
          <el-form-item >
            <el-input v-model="dataForm.cancer" placeholder="cancer type" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-input v-model="dataForm.gen" placeholder="gen" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-input v-model="dataForm.immuneCell" placeholder="immune cell" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-input v-model="dataForm.immunePathway" placeholder="immune pathway" clearable></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item>
            <el-select v-model="dataForm.status" placeholder="status" clearable>
              <el-option
                v-for="item in statusList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="14"  >
          <el-form-item>
            <el-form-item>
              <el-button @click="getDataList()">查询</el-button>
              <el-button v-if="isAuth('rna:rnainfo:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
              <el-button v-if="isAuth('rna:rnainfo:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
              <el-button v-if="isAuth('rna:rnainfo:download')" type="danger" @click="dataDownload()">数据下载</el-button>
              <el-button v-if="isAuth('rna:rnainfo:download')" type="success" @click="templateDownload()">模板下载</el-button>
              <el-button v-if="isAuth('rna:rnainfo:import')" type="warning" @click="toLead = true">数据导入</el-button>
              <el-dialog
                title="导入LmmRNA数据"
                :visible.sync="toLead"
                width="30%"
                center>
                <el-upload
                  class="upload-demo"
                  ref="upload"
                  action=""
                  :limit= 1
                  :on-change = 'changeFile'
                  :on-preview="handlePreview"
                  :on-remove="handleRemove"
                  :file-list="fileList"
                  :auto-upload="false">
                  <el-button slot="trigger" size="small" type="warning">选取文件</el-button>
                </el-upload>
                <span slot="footer" class="dialog-footer">
                <el-button @click="cancle">取 消</el-button>
                <el-button type="success" @click="submit">确 定</el-button>
            </span>
              </el-dialog>
            </el-form-item>
          </el-form-item>
        </el-col>
        <el-col>

        </el-col>


      </el-row>




    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand"   >
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
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="dataId"
        header-align="center"
        align="center"
        label="id">
      </el-table-column>
      <el-table-column
        prop="geneId"
        header-align="center"
        align="center"
        label="gene_id">
      </el-table-column>
    <el-table-column
        prop="databaseId"
        header-align="center"
        align="center"
        label="database_id">
      </el-table-column>
      <el-table-column
        prop="cancerType"
        header-align="center"
        align="center"
        label="cancer_type">
      </el-table-column>
      <el-table-column
        prop="cancerTypeAd"
        header-align="center"
        align="center"
        label="cancer_type_ad">
      </el-table-column>
      <el-table-column
        prop="geneSymbol"
        header-align="center"
        align="center"
        label="gen_symbol">
      </el-table-column>
      <el-table-column
        prop="geneType"
        header-align="center"
        align="center"
        label="gene_type">
      </el-table-column>

      <el-table-column
        prop="immuneCell"
        header-align="center"
        align="center"
        label="immune_cell">
      </el-table-column>
      <el-table-column
        prop="immunePathway"
        header-align="center"
        align="center"
        label="immune_pathway">
      </el-table-column>
      <!--<el-table-column
        prop="pmid"
        header-align="center"
        align="center"
        label="pmid">
      </el-table-column>-->

      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" size="small" >有效</el-tag>
          <el-tag v-else size="small" type = 'danger'>待审核</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.dataId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.dataId)">删除</el-button>
        </template>
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
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './rnainfo-add-or-update'
  export default {
    data () {
      return {
        dataForm: {

        },
        statusList: [
          {
            value: 0,
            label: '有效'
          },
          {
            value: 1,
            label: '待审核'
          }

        ],
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        toLead: false,
        fileList: []
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
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
            'status': this.dataForm.status
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
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.dataId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/rna/rnainfo/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      // 数据下载
      dataDownload () {
        this.$http({
          url: this.$http.adornUrl('/rna/rnainfo/downloadRnaData'),
          method: 'get',
          params: this.$http.adornParams({
            'cancer': this.dataForm.cancer,
            'gen': this.dataForm.gen,
            'immuneCell': this.dataForm.immuneCell,
            'immunePathway': this.dataForm.immunePathway,
            'status': this.dataForm.status
          }),
          responseType: 'blob'
        }).then(({data}) => {
          let blob = new Blob([data], {
            type: 'application/vnd.ms-excel'      // 将会被放入到blob中的数组内容的MIME类型
          })
          let objectUrl = URL.createObjectURL(blob)  // 生成一个url
          window.location.href = objectUrl   // 浏览器打开这个url
        })
      },
      // 模板下载
      templateDownload () {
        this.$http({
          url: this.$http.adornUrl('/rna/rnainfo/downloadRnaTemplate'),
          method: 'get',
          responseType: 'blob'
        }).then(({data}) => {
          let blob = new Blob([data], {
            type: 'application/vnd.ms-excel'      // 将会被放入到blob中的数组内容的MIME类型
          })
          let objectUrl = URL.createObjectURL(blob)  // 生成一个url
          window.location.href = objectUrl   // 浏览器打开这个url
        })
      },

      //
      isExcel (file) {
        const isExcel = (file.type === 'application/vnd.ms-excel' ||
        file.type === 'application/x-excel' ||
        file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')
        return isExcel
      },
      // 上传文件
      changeFile (file) {
        this.fileList = []
        this.fileList.push(file)
      },
      // 取消提交文件
      cancle () {
        this.fileList = []
        this.toLead = false
      },
      // 提交文件
      submit () {
        debugger
        for (let i = 0; i < this.fileList.length; i++) {
          // eslint-disable-next-line no-undef
          if (!this.isExcel(this.fileList[i].raw)) {
            this.$message.error('请导入excel类型的文本')
            return
          }
        }

        let file = this.fileList[0]
        let param = new FormData()
        param.append('file', file.raw)
        this.$http({
          url: this.$http.adornUrl('/rna/rnainfo/importRnaData'),
          method: 'post',
          data: param
        }).then(({data}) => {
          this.fileList = []
          this.toLead = false
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      // 删除文件
      handleRemove (file, fileList) {
        this.fileList.splice(0, 1)
      },
      handlePreview (file) {
        console.log(file)
      }
    }
  }
</script>
<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 223px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.demo-table-expand .el-form-item__content{
  width: 50%;
}

.success-row {
  background: #f0f9eb;
}
</style>



