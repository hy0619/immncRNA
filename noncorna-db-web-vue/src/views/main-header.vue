<template>
  <div class="header_main">
    <div class="header_cnt">
      <div class="header_logo" @click="gotoIndex()"><img :src="imageUrl"  style="width: 214px;"></img></div>
      <div id="nav" class="nav">
        <el-menu
          :default-active="activeName"
          mode="horizontal"
          background-color="#ffffff"
          text-color="#484848">
          <sub-menu
            v-for="nav in navs"
            :key="nav.menuId"
            :nav="nav"
            :dynamicMenuRoutes="dynamicMenuRoutes">
          </sub-menu>
        </el-menu>
      </div>
    </div>
  </div>
</template>

<script>
import SubMenu from './main-sub-menu'
export default {
  data () {
    return {
      navs: [],
      activeName: '',
      dynamicMenuRoutes: [],
      imageUrl: require('@/assets/img/logo.jpg')
    }
  },
  components: {
    SubMenu
  },
  created () {
    console.log(this.$route)
    this.activeName = this.$route.meta.navId + ''
    this.navs = JSON.parse(sessionStorage.getItem('navList') || '[]')
    this.dynamicMenuRoutes = JSON.parse(sessionStorage.getItem('dynamicMenuRoutes') || '[]')
  },
  methods: {
    gotoIndex () {
      window.location = '/home'
    }
  }
}
</script>


<style scoped>
.el-menu-item {
  font-size: 20px;
}

.el-submenu__title {
  font-size: 20px;
}
.header_main{
  width: 100%;
  background-color: #ffffff;
  height: 90px;
}
.header_logo{
  width: 214px;
  overflow: hidden;
  float: left;
  align-items: center;
  justify-content: center;
  display: flex;
  height: 90px;
}
.header_cnt{
  width: 1200px;
  margin: 0 auto;
  overflow: hidden;
  height: 90px;
}
.nav{
  float: right;
}
/deep/.el-menu--horizontal{
  border-bottom: 0 !important;
}
/deep/.el-menu--horizontal>.el-menu-item{
  height: 90px !important;
  line-height: 90px !important;
}
/deep/.el-menu--horizontal>.el-menu-item.is-active{
  background-color: #114694 !important;
  color: #ffffff;
}

</style>
