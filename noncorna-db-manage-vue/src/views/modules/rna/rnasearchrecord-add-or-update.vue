<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="ip" prop="ip">
      <el-input v-model="dataForm.ip" placeholder="ip"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="ct">
      <el-input v-model="dataForm.ct" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="gen 搜索条件" prop="gen">
      <el-input v-model="dataForm.gen" placeholder="gen 搜索条件"></el-input>
    </el-form-item>
    <el-form-item label="cancer 搜索条件" prop="cancer">
      <el-input v-model="dataForm.cancer" placeholder="cancer 搜索条件"></el-input>
    </el-form-item>
    <el-form-item label="immune_cell 搜索条件" prop="immuneCell">
      <el-input v-model="dataForm.immuneCell" placeholder="immune_cell 搜索条件"></el-input>
    </el-form-item>
    <el-form-item label="immune_pathway 搜索条件" prop="immunePathway">
      <el-input v-model="dataForm.immunePathway" placeholder="immune_pathway 搜索条件"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          searchId: 0,
          ip: '',
          ct: '',
          gen: '',
          cancer: '',
          immuneCell: '',
          immunePathway: ''
        },
        dataRule: {
          ip: [
            { required: true, message: 'ip不能为空', trigger: 'blur' }
          ],
          ct: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          gen: [
            { required: true, message: 'gen 搜索条件不能为空', trigger: 'blur' }
          ],
          cancer: [
            { required: true, message: 'cancer 搜索条件不能为空', trigger: 'blur' }
          ],
          immuneCell: [
            { required: true, message: 'immune_cell 搜索条件不能为空', trigger: 'blur' }
          ],
          immunePathway: [
            { required: true, message: 'immune_pathway 搜索条件不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.searchId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.searchId) {
            this.$http({
              url: this.$http.adornUrl(`/rna/rnasearchrecord/info/${this.dataForm.searchId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.ip = data.rnaSearchRecord.ip
                this.dataForm.ct = data.rnaSearchRecord.ct
                this.dataForm.gen = data.rnaSearchRecord.gen
                this.dataForm.cancer = data.rnaSearchRecord.cancer
                this.dataForm.immuneCell = data.rnaSearchRecord.immuneCell
                this.dataForm.immunePathway = data.rnaSearchRecord.immunePathway
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/rna/rnasearchrecord/${!this.dataForm.searchId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'searchId': this.dataForm.searchId || undefined,
                'ip': this.dataForm.ip,
                'ct': this.dataForm.ct,
                'gen': this.dataForm.gen,
                'cancer': this.dataForm.cancer,
                'immuneCell': this.dataForm.immuneCell,
                'immunePathway': this.dataForm.immunePathway
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
