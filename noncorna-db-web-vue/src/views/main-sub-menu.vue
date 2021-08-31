<template>

  <el-submenu v-if="nav.children && nav.children.length >= 1" :index="nav.navId + ''">
    <template slot="title">
      <icon-svg :name="nav.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
      <span>{{ nav.name }}</span>
    </template>
    <sub-menu
      v-for="item in nav.children"
      :key="item.navId"
      :nav="item"
      :dynamicMenuRoutes="dynamicMenuRoutes">
    </sub-menu>
  </el-submenu>

  <el-menu-item v-else :index="nav.navId + ''" @click="gotoRouteHandle(nav)">
    <icon-svg :name="nav.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
    <span>{{ nav.name }}</span>
  </el-menu-item>
</template>

<script>
  import SubMenu from './main-sub-menu'
  export default {
    name: 'sub-menu',
    props: {
      nav: {
        type: Object,
        required: true
      },
      dynamicMenuRoutes: {
        type: Array,
        required: true
      }
    },
    components: {
      SubMenu
    },
    computed: {
      sidebarLayoutSkin: {
        get () { return this.$store.state.common.sidebarLayoutSkin }
      }
    },
    created () {
    },
    methods: {
      // 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
      gotoRouteHandle (menu) {
        var route = this.dynamicMenuRoutes.filter(item => item.meta.navId === menu.navId)
        if (route.length >= 1) {
          this.$router.push({ name: route[0].name })
        }
      }
    }
  }
</script>
