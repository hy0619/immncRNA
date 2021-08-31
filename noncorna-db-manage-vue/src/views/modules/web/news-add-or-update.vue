<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="dataForm.title" placeholder="标题" ></el-input>
      </el-form-item>
      <el-form-item label="链接" prop="linkUrl">
        <el-input v-model="dataForm.linkUrl"  placeholder="链接"></el-input>
      </el-form-item>

      <el-form-item label="正文:" prop = "content">
        <el-col >
          <quill-editor
            v-model="dataForm.content"
            style="height: 300px;"
            ref="myQuillEditor"
            :options="editorOption"
          ></quill-editor>
        </el-col>
      </el-form-item>
    </el-form>
    <br/><br/>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit(0)">暂存</el-button>
      <el-button type="danger" @click="dataFormSubmit(1)">发布</el-button>
    </span>
  </el-dialog>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import { quillEditor } from 'vue-quill-editor' // 调用富文本编辑器
import 'quill/dist/quill.snow.css' // 富文本编辑器外部引用样式  三种样式三选一引入即可
export default {
  data () {
    return {
      visible: false,
      roleList: [],
      dataForm: {
        id: 0,
        title: '',
        linkUrl: '',
        content: '',
        status: 0
      },
      editorOption: {
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'], // 加粗 斜体 下划线 删除线
            ['blockquote', 'code-block'], // 引用  代码块
            [{ header: 1 }, { header: 2 }], // 1、2 级标题
            [{ list: 'ordered' }, { list: 'bullet' }], // 有序、无序列表
            [{ script: 'sub' }, { script: 'super' }], // 上标/下标
            [{ indent: '-1' }, { indent: '+1' }], // 缩进
            // [{'direction': 'rtl'}],                         // 文本方向
            [{ size: ['small', false, 'large', 'huge'] }], // 字体大小
            [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
            [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色
            [{ font: [] }], // 字体种类
            [{ align: [] }], // 对齐方式
            ['clean'], // 清除文本格式
            ['link', 'image'] // 链接、图片、视频
          ] // 工具菜单栏配置
        }, // 开启拖拽
        imageResize: {}, // 图片大小可调
        placeholder: '请输入正文',
        theme: 'snow' // 默认主题
      },
      dataRule: {
        title: [
            { required: true, message: '标题不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  components: {
    quillEditor
  },
  methods: {
    init (id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
      if (!this.dataForm.id) {
          // 新增
      } else {
          // 修改
        this.$http({
          url: this.$http.adornUrl(`/web/news/info/${this.dataForm.id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          console.log(11111)
          this.dataForm.id = data.news.newsId
          this.dataForm.title = data.news.title
          this.dataForm.linkUrl = data.news.linkUrl
          this.dataForm.content = data.news.content
          this.dataForm.status = data.news.status
        })
      }
    },
      // 表单提交
    dataFormSubmit (type) {
      if (type === 1) this.dataForm.status = 1
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/web/news/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'newsId': this.dataForm.id || undefined,
              'title': this.dataForm.title,
              'linkUrl': this.dataForm.linkUrl,
              'content': this.dataForm.content,
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
