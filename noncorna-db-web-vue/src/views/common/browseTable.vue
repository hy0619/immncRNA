<template>
  <div>

    <el-container  style="height: 500px; border: 1px solid #eee">
      <el-aside width="250px" style="background-color: rgb(238, 241, 246)">
        <div class="zTree">
         <el-input
          placeholder="Keywords"
          v-model="filterText">
        </el-input> 
          <div class="tree"> <tree :nodes="tree" :setting="setting"  @onClick="handleNodeClick"
           @onCreated="handleCreated" /> </div>
        </div>
      </el-aside>
      <el-main>
        <el-table
          :data="dataList"
          v-loading="dataListLoading"
        >
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline  class="demo-table-expand"   >
                <el-form-item label="PmID:">
                  <span>{{ props.row.pmid }}</span>
                </el-form-item>

                <el-form-item label="TITLE:">
                  <span>{{ props.row.title }}</span>
                </el-form-item>

                <el-form-item label="JOURNAL:">
                  <span>{{ props.row.journal }}</span>
                </el-form-item>
                <el-form-item label="PUB_TIME:">
                  <span>{{ props.row.pubTime }}</span>
                </el-form-item>
                <el-form-item label="ORGANISM:">
                  <span>{{ props.row.organism }}</span>
                </el-form-item>
                <el-form-item label="TISSUE ORIGIN:">
                  <span>{{ props.row.tissueOrigin }}</span>
                </el-form-item>

                <el-form-item label="DATABASE ID:">
                  <span>{{ props.row.databaseId }}</span>
                </el-form-item>
                <el-form-item label="GENE EXP PATTERN:">
                  <span>{{ props.row.geneExpPattern }}</span>
                </el-form-item>
                <el-form-item label="SUVIVAL:">
                  <span>{{ props.row.suvival }}</span>
                </el-form-item>
                <el-form-item label="MAJORTARGETS: ">
                  <span>{{ props.row.majorTargets }}</span>
                </el-form-item>
                <el-form-item label="TARGETGENE:">
                  <span>{{ props.row.targetGene }}</span>
                </el-form-item>
                <el-form-item label="DOWNSTREAM EFFECT: ">
                  <span>{{ props.row.downstreamEffect }}</span>
                </el-form-item>
                <el-form-item label="FUNCTION METHOD:">
                  <span>{{ props.row.functionMethod }}</span>
                </el-form-item>
                <el-form-item label="IMMUNE CELL:">
                  <span>{{ props.row.immuneCell }}</span>
                </el-form-item>
                <el-form-item label="IMMUNE PATHWAY:">
                  <span>{{ props.row.immunePathway }}</span>
                </el-form-item>
                <el-form-item label="IMMUNE ACTIVITY:">
                  <span>{{ props.row.immuneActivity }}</span>
                </el-form-item>
                <el-form-item label="EVIDENCE CODE:">
                  <span>{{ props.row.evidenceCode }}</span>
                </el-form-item>

                <el-form-item label="DOWNSTREAM EFFECT EVIDENCE:">
                  <span>{{ props.row.downstreamEffectEvidence }}</span>
                </el-form-item>
                <el-form-item label="DOWNSTREAM EFFECT METHOD:">
                  <span>{{ props.row.downstreamEffectMethod }}</span>
                </el-form-item>
                <el-form-item label="FUNCTION:">
                  <span>{{ props.row.function }}</span>
                </el-form-item>
                <el-form-item label="FUNCTION EVIDENCE:">
                  <span>{{ props.row.funtionEvidence }}</span>
                </el-form-item>

              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            prop="geneId"
            header-align="center"
            align="center"
            label="Gene ID">
          </el-table-column>
         
          <el-table-column
            prop="cancerType"
            header-align="center"
            align="center"
            label="Cancer Type">
          </el-table-column>
          <el-table-column
            prop="cancerTypeAd"
            header-align="center"
            align="center"
            label="Cancer Type_Ad">
          </el-table-column>
          <el-table-column
            prop="geneSymbol"
            header-align="center"
            align="center"
            label="Gene Name">
          </el-table-column>
          <el-table-column
            prop="geneType"
            header-align="center"
            align="center"
            label="Gene Type">
          </el-table-column>

          <el-table-column
            prop="immuneCell"
            header-align="center"
            align="center"
            label="Immune Cell">
          </el-table-column>
          <el-table-column
            prop="immunePathway"
            header-align="center"
            align="center"
            label="Immune Pathway">
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
<style>
.ztree li a > span:not(.button){
     max-width: 200px;
     text-overflow:ellipsis;
     white-space: nowrap;
     overflow: hidden;
     display: inline-block;
}
.ztree li span.button.add{
     vertical-align: top;
 }

  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 100%;
  }
</style>

<script>
import tree from  'vue-giant-tree'

export default {
  components: {
      tree
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
      this.onSearch(val)
      //this.$refs.tree.filter(val)
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
      tree: [],
      zTree: null , 
      firstNode: true,
      expandNode: [],
      setting: {
        data: {
          simpleData: {
            enable: true,
            pIdKey: "pid"
          }
        },
        view: {
          showIcon: false,
          addHoverDom: this.addHoverDom,
          removeHoverDom: this.removeHoverDom,
          fontCss: function(treeId , treeNode){
            return (treeNode.searchNode) ? {color:"#A60000" , "font-weight" : "bold"} : ""
          }
        }
      }

    }
  },
  methods: {
    
    handleCreated(ztreeObj){ 


     this.zTree = ztreeObj 

     let firstTree = ztreeObj.getNodes()[0]

     ztreeObj.expandNode(firstTree); // 展开第一行 

 }, 



    handleNodeClick (evt , treeId , node) {
      if(!node.children){
        this.dataForm[node.category] = node.name
      }else{
          this.dataForm[node.category] = ""
      }
      this.getDataList();
      
    },

    onSearch(value){

     if(value){ 

         this.zTree.refresh() 

         let nodeList = this.zTree.getNodesByParamFuzzy('name', value) //模糊搜索

         if(this.expandNode.length > 0){ 

             for(let j in this.expandNode){ 

                 this.closeParentNode(this.expandNode[j]) 

             }

            } 

            this.expandNode = [] 

            let timeout = setTimeout(() =>{

                 clearTimeout(timeout) 

                 for(let i in nodeList){ 

                     this.firstNode = true ;
                      this.getParentNode(nodeList[i]) 

                 }

             },300) } 

 }, 

closeParentNode(node){ //关闭之前展开的节点

     if(node){ 

         let parentNode = node.getParentNode() 

         if(parentNode){ 

             this.zTree.expandNode(parentNode,false,false,false) //关闭节点

             this.closeParentNode(parentNode) 

         } 

         this.zTree.expandNode(node,false,false,false)//关闭节点

     } 

 }, 

 getParentNode(node){ 

     let parentNode = node.getParentNode() 

     this.expandNode.push(parentNode) //保存展开节点

      if(this.firstNode){ 

         this.firstNode = false 

         node.searchNode = 'searchNode' 

         this.zTree.expandNode(parentNode,true,false,false)//展开节点

         this.zTree.updateNode(node) ;node.searchNode = '' 

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
