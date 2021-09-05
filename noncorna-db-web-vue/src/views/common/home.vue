<template>
  <div class="mod-home">
    <div class="banner">
      <div class="ban_searcher">
        <div class="banSer_c">
          <div class="ban_til">Welcome to ImmncRNA</div>
          <div class="searcher_box">
            <el-input
                      v-model="keywords"
                      placeholder="Keywords"
                      prefix-icon="el-icon-search"
                      style="width: 88%;">
            </el-input>
            <el-button type="primary" @click="search()" style="height:50px;border-radius: 0;">Search</el-button>
          </div>
        </div>
      </div>
    </div>
    <div class="index_main">
      <div style="overflow: hidden">
        <div class="main_left">
          <!--  介绍    -->
          <div class="welcome">
            <div class="weclome_js">
              <div class="welcome_til">
                <p>Welcome to ImmncRNA</p>
<!--                <a href="#">Learn More+</a>-->
              </div>
<!--              <p class="welcome_txt" v-html="webconfig.description"></p>-->
              <p class="welcome_ny">Immncrna is a set of specialist databases related to the study of the relationship among tumors, immunity, and ncRNAs. This study provides a database resource for future studies of tumors and immune-related ncRNAs.    Each entry   contains  detailed information on references, PMID, gene symbol, gene type, cancer type, tissue origin, organism, target gene, downstream effect, function, immune cells and other information. This database includes the following features and data: links between  immune-related ncRNAs and target genes; proofs between immune-related ncRNAs and functions; lists of ncRNA-related immune cells and immune pathway. We believe it could be severed as a valuable resource for understanding the functions of ncRNAs,especially immune-related ncRNAs, and advancing the development of immunotherapy.</p>
            </div>

          </div>
        </div>
        <div class="main_right">
          <div>
            <div id="svgTemplate"></div>
          </div>
        </div>
      </div>

      <div style="overflow: hidden">
        <!--    Statistics      -->
        <div class="statisticsMain">
          <div class="quickTil">Statistics</div>
          <div>

            <div class="statistBox2 statistBox">
              <p>GENE ID:</p>
              <span>{{ genCnt }}</span>
            </div>
            <div class="statistBox3 statistBox">
              <p>CANCER TYPE:</p>
              <span>{{ cancerTypeCnt }}</span>
            </div>

            <div class="statistBox1 statistBox">
              <p>IMMUNE CELL:</p>
              <span>{{ immuneCellCnt }}</span>
            </div>
          </div>
        </div>

        <!--    news    -->
        <div class="newsMain">

          <div class="newsTil">
             Latest News
           </div>
           <div class="newsCnt">
             <ul class="newsList">
               <li v-for = "news in topNews">
                 <a target="_blank" >
                   <p>{{ news.title }}</p>
                   <span>{{ news.ct }}</span>
                 </a>
               </li>
             </ul>
           </div>
        </div>

        <!--    searcher    -->
        <div class="quickSerMain">
          <div class="quickTil">Most Search</div>
          <svg :width='width' :height='height' @mousemove='listener($event)'>
            <a :href="tag.href" target="_blank" v-for='tag in tags' >
              <text :x='tag.x' :y='tag.y':fill="tag.fill" :font-size='15 * (600/(600-tag.z))' :fill-opacity='((400+tag.z)/600)'>{{tag.text}}</text>
            </a>
          </svg>
        </div>
      </div>

      <div style="overflow: hidden;margin-top: 12px;margin-bottom: 10px;">
<!--        <div class="chart-1 chart-box" id="J_chartPieBox2">-->
<!--        </div>-->
<!--        <div class="chart-2 chart-box" id = "J_chartPieBox"></div>-->
      </div>
    </div>

  </div>
</template>
<script>
import Vue from 'vue/dist/vue.esm.js'

import * as d3 from 'd3'// 引入d3

import echarts from 'echarts'

export default {
  data () {
    return {
      keywords: '',
      width: 384,
      height: 354,
      tagsNum: 20,
      RADIUS: 130,
      speedX: Math.PI / 360,
      speedY: Math.PI / 360,
      tags: [],
      webconfig: {},
      topNews: [],
      immuneCellCnt: 0,
      genCnt: 0,
      cancerTypeCnt: 0,
      chartPie: null

    }
  },
  computed: {
    CX () {
      return this.width / 3
    },
    CY () {
      return this.height / 2
    }
  },
  created: function () {
    var _this = this
    document.onkeydown = function (e) {   // 按下回车提交
      let key = window.event.keyCode
        // eslint-disable-next-line eqeqeq
      if (key == 13) {
        _this.search()
      }
    }

    this.getSvg()
  },
  async  mounted () { // 使球开始旋转
    window['handleClick'] = () => {
      this.takePhoto()
    }
    setInterval(() => {
      this.rotateX(this.speedX)
      this.rotateY(this.speedY)
    }, 30)

    // this.initChartPie()
    // this.initChartPie2()
  },
  activated () {
    this.getWebConfig()
    this.getTopNews()
    this.getStatisticsBaseInfo()
    this.getTags()

    if (this.chartLine) {
      this.chartLine.resize()
    }
    if (this.chartBar) {
      this.chartBar.resize()
    }
    if (this.chartPie) {
      this.chartPie.resize()
    }
    if (this.chartScatter) {
      this.chartScatter.resize()
    }
  },
  watch: {
    photoResult: {
      handler (newVal, oldVal) {
        this.getSvg()
      },
      deep: true
    }
  },
  // https://editor.method.ac/#delete
  methods: {
    getSvg () {
      const xhr = new XMLHttpRequest()
      this.svgUrl = '../../../static/img/Figure1-4-2.svg' // svg的绝对地址，在浏览器中打开能看到的那个
      xhr.open('GET', this.svgUrl, true)
      xhr.send()

      xhr.addEventListener('load', () => {
        // ① 获取svg的dom
        const resXML = xhr.responseXML
        this.svgDom = resXML.documentElement.cloneNode(true)     // console.log(this.svgDom);
        // ② 添加click事件
        let tags = this.svgDom.querySelectorAll('a')
        for (let idx in tags) {
          if (tags[idx] && typeof tags[idx] !== 'function' && idx !== 'length') {
            let keywords = tags[idx].getAttribute('id').split('_')[1]
            tags[idx].setAttribute('xlink:href'
              , 'search-result?keyWords=' + keywords)
          }
        }
        // ④ 将svgDom对象转换成vue的虚拟dom，创建实例并挂载到元素上
        var oSerializer = new XMLSerializer()
        var sXML = oSerializer.serializeToString(this.svgDom)

        var Profile = Vue.extend({
          template: "<div id='svgTemplate'>" + sXML + '</div>'
        })
        new Profile().$mount('#svgTemplate')

        let path = document.querySelectorAll('[id^=organ_]')

        // let path = this.svgDom.querySelectorAll('[id^=organ_]')
        for (let tissue of path) {
          d3.select(tissue).on('mouseover', function () {
            let organId = this.getAttribute('id')
            let fontId = organId.split('_')[0] + '_' + organId.split('_')[1] + '_text'
            console.log('fontId:' + fontId)
            let organPicId = organId.split('_')[0] + '_' + organId.split('_')[1] + '_pic'
            console.log('organPicId:' + organPicId)
            // console.log(document.getElementById(fontId))
            document.getElementById(fontId).style.fontWeight = '900'
            document.getElementById(organPicId).style.stroke = 'black'
            document.getElementById(organPicId).style.strokeWidth = '1'
            document.getElementById(fontId).style.fontSize = '13'
          }).on('mouseout', function () {
            let organId = this.getAttribute('id')
            let fontId = organId.split('_')[0] + '_' + organId.split('_')[1] + '_text'
            let organPicId = organId.split('_')[0] + '_' + organId.split('_')[1] + '_pic'
            // console.log(document.getElementById(fontId))
            document.getElementById(fontId).style.fontWeight = '400'
            // document.getElementById(organPicId).style.stroke = 'black'
            document.getElementById(organPicId).style.strokeWidth = '0.7'
            document.getElementById(fontId).style.fontSize = '10.29'
          })
        }

       /* d3.select(path).on('mouseover', function () {
          console.log('------------------')
        }).on('mouseout', function () {

        })

        console.log(path) */
        // eslint-disable-next-line no-unused-vars
        // for (let tissue of path) {
        //   console.log(tissue)
        //   d3.select(tissue).on('mouseover', function () {
        //     console.log('------------------')
        //   }).on('mouseout', function () {
        //
        //   })
        // }
        /* let brain = document.getElementById('organ_Brain_pic')
        console.log(d3)
        d3.select(brain).on('mouseover', function () {
          console.log('------------------')
        }).on('mouseout', function () {

        }) */
      })
    },

    beforeDestroy () {
      this.svgDom = null
    },
    takePhoto () {
      console.log(2222)
    },

    rotateX (speedX) {
      var cos = Math.cos(speedX)
      var sin = Math.sin(speedX)
      for (let tag of this.tags) {
        var y1 = (tag.y - this.CY) * cos - tag.z * sin + this.CY
        var z1 = tag.z * cos + (tag.y - this.CY) * sin
        tag.y = y1
        tag.z = z1
      }
    },
    rotateY (speedY) {
      var cos = Math.cos(speedY)
      var sin = Math.sin(speedY)
      for (let tag of this.tags) {
        var x1 = (tag.x - this.CX) * cos - tag.z * sin + this.CX
        var z1 = tag.z * cos + (tag.x - this.CX) * sin
        tag.x = x1
        tag.z = z1
      }
    },
    listener (event) { // 响应鼠标移动
      var x = event.clientX - this.CX
      var y = event.clientY - this.CY
      this.speedX = x * 0.0001 > 0 ? Math.min(this.RADIUS * 0.00002, x * 0.0001) : Math.max(-this.RADIUS * 0.00002, x * 0.0001)
      this.speedY = y * 0.0001 > 0 ? Math.min(this.RADIUS * 0.00002, y * 0.0001) : Math.max(-this.RADIUS * 0.00002, y * 0.0001)
    },
    getWebConfig () {
      this.$http({
        url: this.$http.adornUrl('/web/config/get'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.webconfig = data.config
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    getStatisticsBaseInfo () {
      this.$http({
        url: this.$http.adornUrl('/web/statistics/baseInfo'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.immuneCellCnt = data.immuneCellCnt
          this.genCnt = data.genCnt
          this.cancerTypeCnt = data.cancerTypeCnt
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    search () {
      let routeUrl = this.$router.resolve({
        path: '/search-result',
        query: { 'keyWords': this.keywords }
      })
      window.open(routeUrl.href, '_blank')
       // this.$router.push({path: '/search-result', query: { 'keyWords': 1 }})
    },

    getColor (catagory) {
        // eslint-disable-next-line eqeqeq,brace-style
      if (catagory == 'cancerType') { return 'rgb(154,110,24)' }
          // eslint-disable-next-line eqeqeq
      else if (catagory == 'geneId') { return 'rgb(18,220,151)' } else if (catagory == 'immuneCell') return 'rgb(227,211,81)'
        // eslint-disable-next-line eqeqeq
      else if (catagory == 'genSymbol') { return 'rgb(193,41,21)' } else if (catagory == 'cancerTypeAd') return 'rgb(30,29,33)'
      else return 'rgb(137,239,12)'
    },

    getTags () {
      this.tags = []
      this.$http({
        url: this.$http.adornUrl('/rna/rnasearchrecord/web/quickSearchTags'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.tagsNum = data.tagNum
          let tagsTemp = []
          console.log(data.tags)
          for (let key in data.tags) {
            data.tags[key].forEach(tag => {
              let temp = {}
              temp.catagory = key
              temp.text = tag
              tagsTemp.push(temp)
            })
          }

          for (let i = 0; i < tagsTemp.length; i++) {
            let tag = {}
            let k = -1 + (2 * (i + 1) - 1) / this.tagsNum
            let a = Math.acos(k)
            let b = a * Math.sqrt(this.tagsNum * Math.PI)
            tag.text = tagsTemp[i].text
            tag.x = this.CX + this.RADIUS * Math.sin(a) * Math.cos(b)
            tag.y = this.CY + this.RADIUS * Math.sin(a) * Math.sin(b)
            tag.z = this.RADIUS * Math.cos(a)
            tag.href = '/search-result?keyWords=' + tagsTemp[i].text
            tag.fill = this.getColor(tagsTemp[i].catagory)
            this.tags.push(tag)
          }
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    getTopNews () {
      this.$http({
        url: this.$http.adornUrl('/web/news/topNews?topSize=' + 5),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.topNews = data.topNews
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    // 饼状图
    initChartPie () {
      var chartPie = echarts.init(document.getElementById('J_chartPieBox'))

      var option = {
        backgroundColor: '#ffffff',
        title: {
          text: 'CANCER TYPE',
          left: 'center',
          top: 20
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 10,
          top: 20,
          bottom: 20,
          data: []
        },
        series: [
          {
            name: 'CANCER TYPE',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: [

            ],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(168,45,45,0.5)'
              },
              normal: {

              }
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0
              }
            },
            label: {
              normal: {
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                }
              }
            },
            labelLine: {
              normal: {
                lineStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                },
                smooth: 0.2,
                length: 10,
                length2: 20
              }
            }
          }
        ]
      }
      this.$http({
        url: this.$http.adornUrl('/web/statistic/getCntByCol/cancer_type'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          option.legend.data = []
          option.series[0].data = []
          for (let one of data.res) {
            let info = {}
            info.name = one.cancer_type
            info.value = one.cnt
            option.series[0].data.push(info)
            option.legend.data.push(info.name)
          }

          chartPie.setOption(option)
          window.addEventListener('resize', () => {
            this.chartPie.resize()
          })

          console.log(option.series.data)
        } else {
          this.$message.error(data.msg)
        }
      })
    },

    initChartPie2 () {
      var chartPie = echarts.init(document.getElementById('J_chartPieBox2'))

      var option = {
        backgroundColor: '#ffffff',
        title: {
          text: 'GENE TYPE',
          left: 'center',
          top: 20,
          textStyle: {
            color: '#ccc'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 0,
          top: 20,
          bottom: 20,
          data: []
        },
        series: [
          {
            name: 'CANCER TYPE',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: [

            ],
            roseType: 'radius',
            itemStyle: {
              color: '#c23531',
              shadowBlur: 200,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
              return Math.random() * 200
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0
              }
            },
            label: {
              color: 'rgba(255, 255, 255, 0.3)'
            },
            labelLine: {
              normal: {
                length: 1
              },
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              }

            }
          }
        ]
      }
      this.$http({
        url: this.$http.adornUrl('/web/statistic/getCntByCol/gene_type'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          option.legend.data = []
          option.series[0].data = []
          for (let one of data.res) {
            let info = {}
            info.name = one.gene_type
            info.value = one.cnt
            option.series[0].data.push(info)
            option.legend.data.push(info.name)
          }

          chartPie.setOption(option)

          window.addEventListener('resize', () => {
            this.chartPie.resize()
          })
        } else {
          this.$message.error(data.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
  .main_right{
    float: right;
    width: 650px;
  }
  .main_left{
    width: 520px;
    float: left;
  }
  .welcome_ny{
    text-indent: 2em;
    font-size: 20px;
    line-height: 32px;
    text-align:justify;
    text-justify:inter-ideograph;
    color: #333333;
  }
  .chart-box{
    float: left;
    height: 500px;
  }
  .chart-1{
    width: 390px;
    margin-right: 10px;
  }
  .chart-2{
    width: 700px;
    margin-right: 10px;
  }
  /*.chart-3{*/
  /*  width: 340px;*/
  /*  margin-right: 10px;*/
  /*}*/

  .welcome{
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .welcome_pic{
    flex-shrink: 0;
    width: 115px;
    height: 115px;
    overflow: hidden;
    border-radius: 50%;
    border: 5px solid #ffffff;
    background: #114694;
  }
  .newsList{
    padding-left: 20px;
    padding-top: 10px;
  }
  .newsList li{
    line-height: 20px;
    font-size: 17px;
  }
  .newsList li a {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 460px;
    height: 42px;
  }
  .newsList li a:hover{
    text-decoration: none;
  }
  .newsList li p{
    float: left;
    padding: 0 !important;
    margin: 0 !important;
    overflow: hidden;
    width: 100%;
    white-space: nowrap;
    text-overflow:ellipsis;
    font-size: 16px;
    color: #464646;
  }
  .newsList li a:hover p,.newsList li a:hover span{
    color: #114694;
  }
  .newsList li span{
    float: right;
    background-color: #d8dde3;
    color: #525252;
    font-size: 14px;
    border-radius: 20px;
    height: 24px;
    line-height: 24px;
    padding: 0 7px;
    flex-shrink: 0;
  }
  .statisList li {
    list-style: circle;
  }
  .banner{
    width: 100%;
    height: 400px;
    background: url("~@/assets/img/banner.jpg") no-repeat center center;
    position: relative;
  }
  .index_main{
    width: 1200px;
    margin: 0 auto;
    padding-top:35px;
  }
  .mod-home{
    background-color: #ebeff4;
  }
  .welcome_til{
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: #114694;
  }
  .welcome_til p{
    font-size: 26px;
    padding: 0 !important;
    margin: 0 !important;
    font-weight: 700;
  }
  .welcome_til a {
    color: #114694;
    font-size: 16px;
  }
  .welcome_txt{
    line-height: 24px;
    color: #363636;
    font-size: 16px;
    font-weight: 600;
    height: 210px;
    margin: 0 !important;
  }
  .weclome_js{
    padding-left: 15px;
    width: 100%;
    box-sizing: border-box;
  }
  .ban_searcher{
    position: absolute;
    top:100px;
    left: 0;
    width: 100%;
  }
  .ban_til{
    color: #ffffff;
    font-size: 20px;
    text-shadow:6px 6px 6px #76ace1;
    letter-spacing:5px;
    font-weight: 500;
    font-size: 32px;
  }
  .banSer_c{
    width: 700px;
    margin: 0 auto;
  }
  .searcher_box{
    width: 630px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 80px;
  }
  .index_bt{
    overflow: hidden;
    width: 100%;
    margin-top: 20px;
  }
  .statisticsMain{
    width: 275px;
    float: left;
    height: 307px;
  }
  .newsMain{
    width: 480px;
    float: left;
    padding-left: 15px;
  }
  .quickSerMain{
    width: 395px;
    float: right;
  }
  .newsTil{
    background: url("~@/assets/img/newstil.jpg") no-repeat;
    background-size: 100% 100%;
    width: 230px;
    height: 46px;
    line-height: 46px;
    font-size: 22px;
    color: #114694;
    font-weight: 700;
    text-indent: 1.5em;
  }
  .newsCnt{
    background: url("~@/assets/img/newsbg.jpg") no-repeat left top;
    background-size: 100% 100%;
    width: 480px;
    height: 247px;
  }
  .quickTil{
    color: #114694;
    font-size: 24px;
    font-weight: 700;
    padding-top: 10px;
  }
  .statistBox{
    border-radius: 36px;
    width: 270px;
    height: 58px;
    color: #ffffff;
    margin-top: 24px;
    overflow: hidden;
    line-height: 58px;
  }
  .statistBox1{
    background-color: #31a8ac;
  }
  .statistBox2{
    background-color: #1e69d9;
  }
  .statistBox3{
    background-color: #d5ac15;
  }
  .statistBox p{
    float: left;
    width: 130px;
    margin: 0 !important;
    padding: 0 0 0 20px !important;
    font-size: 22px;
  }
  .statistBox span{
    float: right;
    padding-right: 22px;
    font-size: 30px;
    font-weight: bold;
  }
  /deep/ .el-input--medium .el-input__inner{
    height: 50px;
    line-height: 50px;
    border-radius: 4px 0 0 4px;
  }
</style>

