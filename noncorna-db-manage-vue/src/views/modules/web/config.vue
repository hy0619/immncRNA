<template>
  <div >
    <el-form :inline="true"  >
      <el-form-item>
        <el-button type="primary" @click="saveConfig()">保存</el-button>
      </el-form-item>
    </el-form>
    <el-form ref="config" label-position="left" :model="config"  size="medium" label-width="80px">

      <!--<el-form-item label="logo图片:">
        <el-col :span="18">
          <el-upload
            class="avatar-uploader"
            :action='url'
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="config.logo" :src="config.logo" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-col>
      </el-form-item>-->

     <!-- <el-form-item label="网站简介:">
        <el-col :span="18">
          <el-input :rows="5" type="textarea"    v-model="config.introduce"></el-input>
        </el-col>
      </el-form-item>

      <el-form-item label="底部文案:">
        <el-col :span="18">
          <el-input :rows="5" type="textarea"  v-model="config.footerContent"></el-input>
        </el-col>
      </el-form-item>-->



      <el-form-item label="网站描述:">
        <el-col :span="18">
          <quill-editor
            v-model="config.description"
            style="height: 300px;"
            ref="myQuillEditor"
            :options="editorOption"
          ></quill-editor>
        </el-col>
      </el-form-item>

      <br/> <br/> <br/> <br/> <br/><br/> <br/>
      <el-form-item label="帮助内容:">
        <el-col :span="18">
          <quill-editor
            v-model="config.helpContent"
            style="height: 300px;"
            ref="myQuillEditor"
            :options="editorOption">
          </quill-editor>
        </el-col>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import { quillEditor } from 'vue-quill-editor' // 调用富文本编辑器
import 'quill/dist/quill.snow.css' // 富文本编辑器外部引用样式  三种样式三选一引入即可
export default {
  data () {
    return {
      url: '',
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
      config: {
        id: null,
        introduce: '',
        description: '',
        logo: '',
        footerContent: '',
        helpContent: ''
      }

    }
  },
  components: {
    quillEditor
  },
  mounted () {
    this.editor = this.$refs.myQuillEditor.quill
    this.url = this.$http.adornUrl(`/sys/oss/upload?token=${this.$cookie.get('token')}`)
    console.log(this.url)
  },
  activated () {
    this.getWebConfig()
  },
  methods: {
    handleAvatarSuccess (res, file) {
      console.log(res)
      this.config.logo = res.url
    },
    beforeAvatarUpload (file) {
      const isImage = file.type === 'image/jpg' || file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('上传头像图片只能是图片格式:jpg,jpeg,png,gif!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isImage && isLt2M
    },
    getWebConfig () {
      this.$http({
        url: this.$http.adornUrl('/web/config/get'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        this.config = data.config
      })
    },
    saveConfig () {
      this.$refs['config'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/web/config/save`),
            method: 'post',
            data: this.$http.adornData(this.config)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
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

<style>
.avatar-uploader .el-upload {
border: 1px dashed #d9d9d9;
border-radius: 6px;
cursor: pointer;
position: relative;
overflow: hidden;
}
.avatar-uploader .el-upload:hover {
border-color: #409EFF;
}
.avatar-uploader-icon {
font-size: 28px;
color: #8c939d;
width: 178px;
height: 178px;
line-height: 178px;
text-align: center;
}
.avatar {
width: 178px;
height: 178px;
display: block;
}
</style>
