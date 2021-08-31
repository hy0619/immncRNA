<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="200px">
      <el-form-item label="cancer_type" prop="cancerType">
        <el-input v-model="dataForm.cancerType" placeholder="cancer_type"></el-input>
      </el-form-item>
      <el-form-item label="cancer_type_ad" prop="cancerTypeAd">
        <el-input v-model="dataForm.cancerTypeAd" placeholder="cancer_type_ad"></el-input>
      </el-form-item>
      <el-form-item label="gene_id" prop="geneId">
        <el-input v-model="dataForm.geneId" placeholder="gene_id"></el-input>
      </el-form-item>
      <el-form-item label="gene_symbol" prop="geneSymbol">
        <el-input v-model="dataForm.geneSymbol" placeholder="gene_symbol"></el-input>
      </el-form-item>
      <el-form-item label="database_id" prop="databaseId">
        <el-input v-model="dataForm.databaseId" placeholder="database_id"></el-input>
      </el-form-item>
      <el-form-item label="gene_type" prop="geneType">
        <el-input v-model="dataForm.geneType" placeholder="gene_type"></el-input>
      </el-form-item>
      <el-form-item label="gene_exp_pattern" prop="geneExpPattern">
        <el-input v-model="dataForm.geneExpPattern" placeholder="gene_exp_pattern"></el-input>
      </el-form-item>

      <el-form-item label="immune_cell" prop="immuneCell">
        <el-input v-model="dataForm.immuneCell" placeholder="immune_cell"></el-input>
      </el-form-item>
      <el-form-item label="immune_pathway" prop="immunePathway">
        <el-input v-model="dataForm.immunePathway" placeholder="immune_pathway"></el-input>
      </el-form-item>
      <el-form-item label="immune_activity" prop="immuneActivity">
        <el-input v-model="dataForm.immuneActivity" placeholder="immune_activity"></el-input>
      </el-form-item>

      <el-form-item label="organism" prop="organism">
        <el-input v-model="dataForm.organism" placeholder="organism"></el-input>
      </el-form-item>
      <el-form-item label="tissue_origin" prop="tissueOrigin">
        <el-input v-model="dataForm.tissueOrigin" placeholder="tissue_origin"></el-input>
      </el-form-item>

      <el-form-item label="suvival" prop="suvival">
        <el-input v-model="dataForm.suvival" placeholder="suvival"></el-input>
      </el-form-item>
      <el-form-item label="major_targets" prop="majorTargets">
        <el-input v-model="dataForm.majorTargets" placeholder="major_targets"></el-input>
      </el-form-item>
      <el-form-item label="target_gene" prop="targetGene">
        <el-input v-model="dataForm.targetGene" placeholder="target_gene"></el-input>
      </el-form-item>
      <el-form-item label="downstream_effect" prop="downstreamEffect">
        <el-input v-model="dataForm.downstreamEffect" placeholder="downstream_effect"></el-input>
      </el-form-item>
      <el-form-item label="downstream_effect_evidence" prop="downstreamEffectEvidence">
        <el-input v-model="dataForm.downstreamEffectEvidence" placeholder="downstream_effect_evidence"></el-input>
      </el-form-item>
      <el-form-item label="downstream_effect_method" prop="downstreamEffectMethod">
        <el-input v-model="dataForm.downstreamEffectMethod" placeholder="downstream_effect_method"></el-input>
      </el-form-item>
      <el-form-item label="function" prop="function">
        <el-input v-model="dataForm.function" placeholder="function"></el-input>
      </el-form-item>
      <el-form-item label="funtion_evidence" prop="funtionEvidence">
        <el-input v-model="dataForm.funtionEvidence" placeholder="funtion_evidence"></el-input>
      </el-form-item>
      <el-form-item label="function_method" prop="functionMethod">
        <el-input v-model="dataForm.functionMethod" placeholder="function_method"></el-input>
      </el-form-item>
      <el-form-item label="evidence_code" prop="evidenceCode">
        <el-input v-model="dataForm.evidenceCode" placeholder="evidence_code"></el-input>
      </el-form-item>
      <el-form-item label="pmid" prop="pmid">
        <el-input v-model="dataForm.pmid" placeholder="pmid"></el-input>
      </el-form-item>
      <el-form-item label="tilte" prop="tilte">
        <el-input v-model="dataForm.tilte" placeholder="tilte"></el-input>
      </el-form-item>
      <el-form-item label="journal" prop="journal">
        <el-input v-model="dataForm.journal" placeholder="journal"></el-input>
      </el-form-item>
      <el-form-item label="published" prop="pubTime">
        <el-input v-model="dataForm.pubTime" placeholder="published"></el-input>
      </el-form-item>
      <!--<el-form-item label="status" prop="status">
        <el-input v-model="dataForm.status" placeholder="status"></el-input>
      </el-form-item>-->
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
          dataId: 0,
          pmid: '',
          tilte: '',
          journal: '',
          pubTime: '',
          organism: '',
          tissueOrigin: '',
          cancerType: '',
          cancerTypeAd: '',
          geneId: '',
          geneSymbol: '',
          databaseId: '',
          geneType: '',
          geneExpPattern: '',
          suvival: '',
          majorTargets: '',
          targetGene: '',
          downstreamEffect: '',
          downstreamEffectEvidence: '',
          downstreamEffectMethod: '',
          function: '',
          funtionEvidence: '',
          functionMethod: '',
          immuneCell: '',
          immunePathway: '',
          immuneActivity: '',
          evidenceCode: '',
          status: '0'
        },
        dataRule: {
          cancerType: [
            { required: true, message: 'cancer_type不能为空', trigger: 'blur' }
          ],
          cancerTypeAd: [
            { required: true, message: 'cancer_type_ad不能为空', trigger: 'blur' }
          ],
          geneId: [
            { required: true, message: 'gene_id不能为空', trigger: 'blur' }
          ],
          geneSymbol: [
            { required: true, message: 'gene_symbol不能为空', trigger: 'blur' }
          ],
          databaseId: [
            { required: true, message: 'database_id不能为空', trigger: 'blur' }
          ],
          geneType: [
            { required: true, message: 'gene_type不能为空', trigger: 'blur' }
          ],
          immuneCell: [
            { required: true, message: 'immune_cell不能为空', trigger: 'blur' }
          ],
          immunePathway: [
            { required: true, message: 'immune_pathway不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.dataId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.dataId) {
            this.$http({
              url: this.$http.adornUrl(`/rna/rnainfo/info/${this.dataForm.dataId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.pmid = data.rnaInfo.pmid
                this.dataForm.rnaId = data.rnaInfo.rnaId
                this.dataForm.tilte = data.rnaInfo.tilte
                this.dataForm.journal = data.rnaInfo.journal
                this.dataForm.pubTime = data.rnaInfo.pubTime
                this.dataForm.organism = data.rnaInfo.organism
                this.dataForm.tissueOrigin = data.rnaInfo.tissueOrigin
                this.dataForm.cancerType = data.rnaInfo.cancerType
                this.dataForm.cancerTypeAd = data.rnaInfo.cancerTypeAd
                this.dataForm.geneId = data.rnaInfo.geneId
                this.dataForm.geneSymbol = data.rnaInfo.geneSymbol
                this.dataForm.databaseId = data.rnaInfo.databaseId
                this.dataForm.geneType = data.rnaInfo.geneType
                this.dataForm.geneExpPattern = data.rnaInfo.geneExpPattern
                this.dataForm.suvival = data.rnaInfo.suvival
                this.dataForm.majorTargets = data.rnaInfo.majorTargets
                this.dataForm.targetGene = data.rnaInfo.targetGene
                this.dataForm.downstreamEffect = data.rnaInfo.downstreamEffect
                this.dataForm.downstreamEffectEvidence = data.rnaInfo.downstreamEffectEvidence
                this.dataForm.downstreamEffectMethod = data.rnaInfo.downstreamEffectMethod
                this.dataForm.function = data.rnaInfo.function
                this.dataForm.funtionEvidence = data.rnaInfo.funtionEvidence
                this.dataForm.functionMethod = data.rnaInfo.functionMethod
                this.dataForm.immuneCell = data.rnaInfo.immuneCell
                this.dataForm.immunePathway = data.rnaInfo.immunePathway
                this.dataForm.immuneActivity = data.rnaInfo.immuneActivity
                this.dataForm.evidenceCode = data.rnaInfo.evidenceCode
                this.dataForm.status = data.rnaInfo.status
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
              url: this.$http.adornUrl(`/rna/rnainfo/${!this.dataForm.dataId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'dataId': this.dataForm.dataId || undefined,
                'pmid': this.dataForm.pmid,
                'rnaId': this.dataForm.rnaId,
                'tilte': this.dataForm.tilte,
                'journal': this.dataForm.journal,
                'pubTime': this.dataForm.pubTime,
                'organism': this.dataForm.organism,
                'tissueOrigin': this.dataForm.tissueOrigin,
                'cancerType': this.dataForm.cancerType,
                'geneId': this.dataForm.geneId,
                'geneSymbol': this.dataForm.geneSymbol,
                'databaseId': this.dataForm.databaseId,
                'geneType': this.dataForm.geneType,
                'geneExpPattern': this.dataForm.geneExpPattern,
                'suvival': this.dataForm.suvival,
                'majorTargets': this.dataForm.majorTargets,
                'targetGene': this.dataForm.targetGene,
                'downstreamEffect': this.dataForm.downstreamEffect,
                'downstreamEffectEvidence': this.dataForm.downstreamEffectEvidence,
                'downstreamEffectMethod': this.dataForm.downstreamEffectMethod,
                'function': this.dataForm.function,
                'funtionEvidence': this.dataForm.funtionEvidence,
                'functionMethod': this.dataForm.functionMethod,
                'immuneCell': this.dataForm.immuneCell,
                'immunePathway': this.dataForm.immunePathway,
                'immuneActivity': this.dataForm.immuneActivity,
                'evidenceCode': this.dataForm.evidenceCode,
                'status': this.dataForm.status
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
