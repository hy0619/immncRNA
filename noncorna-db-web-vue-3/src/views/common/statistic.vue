<template>
  <div class="mod-demo-echarts">
    <div class="statistInfo">
      <div class="statistInfo_c">
        <div class="statisInfo_l">
          <img src="../../assets/img/pic1.jpg" />
        </div>
        <div class="statisInfo_r">
          <div class="statisInfo_r_til">Welcome to NoncoRNA</div>
          <p class="statisInfo_r_txt">NoncoRNA: a database for experimentally supported Non-Coding RNA and Drug Target associations in Cancer. Cancer is an important cause of morbidity and mortality worldwide, in every world region, and irrespective of the level of human development. In addition, the incidence and mortality of cancer have increased rapidly worldwide in recent years. The reasons</p>
        </div>
      </div>
    </div>
    <div class="statist_cnt">
        <div class="statist_bar">
          <div class="statist_bar_box" style="float: left">
            <div class="chart_til">CANCER TYPE</div>
            <div class="bar_cnt">
              <div id="J_chartPieBox" class="chart-box"></div>
            </div>
          </div>
          <div class="statist_bar_box" style="float: right">
            <div class="chart_til">GENE TYPE</div>
            <div class="bar_cnt">
              <div id="J_chartPieBox2" class="chart-box"></div>
            </div>
          </div>
        </div>
        <div style="margin-top: 25px;">
          <div class="chart_til">DATABASE SEARCH COUNT BEFORE 7 DAYS</div>
          <div class="graphBox" style="height: 380px;">
            <div id="J_chartBarBox" class="chart-box"></div>
          </div>
        </div>
        <div style="margin-top: 25px;">
          <div class="chart_til">DATABASE SEARCH COUNT BEFORE 7 DAYS</div>
          <div class="graphBox" style="height: 380px;">
            <div id="J_chartLineBox" class="chart-box"></div>
          </div>
        </div>
    </div>


  </div>
</template>

<script>
  import echarts from 'echarts'

  export default {
    data () {
      return {
        chartLine: null,
        chartBar: null,
        chartPie: null,
        chartScatter: null
      }
    },
    mounted () {
      this.initChartPie()
      this.initChartPie2()

      this.initChartLine()
      this.initChartBar()
      // this.initChartScatter()
    },
    activated () {
      /* this.initChartPie() */
      // 由于给echart添加了resize事件, 在组件激活时需要重新resize绘画一次, 否则出现空白bug
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
    methods: {
      // 折线图
      initChartLine () {
        var option = {
          'title': {
            'text': ''
          },
          'tooltip': {
            'trigger': 'axis'
          },
          'legend': {
            'data': [ 'GENE[GENE_ID GENE_SYMBOL]', 'CANCER[CANCER_TYPE CANCER TYPE AD]', 'IMMUNE CELL', 'IMMUNE PATHWAY' ]
          },
          'grid': {
            'left': '3%',
            'right': '4%',
            'bottom': '3%',
            'containLabel': true
          },
          'toolbox': {
            'feature': {
              'saveAsImage': { }
            }
          },
          'xAxis': {
            'type': 'category',
            'boundaryGap': false,
            'data': [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
          },
          'yAxis': {
            'type': 'value'
          },
          'series': [
            {
              'name': '邮件营销',
              'type': 'line',
              'stack': '总量',
              'data': [ 120, 132, 101, 134, 90, 230, 210 ]
            },
            {
              'name': '联盟广告',
              'type': 'line',
              'stack': '总量',
              'data': [ 220, 182, 191, 234, 290, 330, 310 ]
            },
            {
              'name': '视频广告',
              'type': 'line',
              'stack': '总量',
              'data': [ 150, 232, 201, 154, 190, 330, 410 ]
            },
            {
              'name': '直接访问',
              'type': 'line',
              'stack': '总量',
              'data': [ 320, 332, 301, 334, 390, 330, 320 ]
            },
            {
              'name': '搜索引擎',
              'type': 'line',
              'stack': '总量',
              'data': [ 820, 932, 901, 934, 1290, 1330, 1320 ]
            }
          ]
        }

        this.$http({
          url: this.$http.adornUrl('/web/statistic/searchCnt'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            option.xAxis.data = data.date

            // GENE[GENE_ID GENE_SYMBOL]', 'CANCER[CANCER_TYPE CANCER TYPE AD]
            let genInfo = {
              name: 'GENE[GENE_ID GENE_SYMBOL]',
              type: 'line',
              data: data.searchCnt.gen
            }
            let cancerInfo = {
              name: 'CANCER[CANCER_TYPE CANCER TYPE AD]',
              type: 'line',
              data: data.searchCnt.cancer
            }
            let immuneCell = {
              name: 'IMMUNE CELL',
              type: 'line',
              data: data.searchCnt.immune_cell
            }
            let immunePath = {
              name: 'IMMUNE PATHWAY',
              type: 'line',
              data: data.searchCnt.immune_pathway
            }
            option.series = []
            option.series.push(genInfo)
            option.series.push(cancerInfo)
            option.series.push(immuneCell)
            option.series.push(immunePath)

            this.chartLine = echarts.init(document.getElementById('J_chartLineBox'))
            this.chartLine.setOption(option)
            window.addEventListener('resize', () => {
              this.chartLine.resize()
            })
          } else {
            this.$message.error(data.msg)
          }
        })


      },
      // 柱状图
      initChartBar () {
        var option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: ['GENE', 'CANCER', 'IMMUNE CELL', 'IMMUNE PATHWAY']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: []
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [

          ]
        }

        this.$http({
          url: this.$http.adornUrl('/web/statistic/searchCnt'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            option.xAxis[0].data = data.date


            let genInfo = {
              name: 'GENE',
              type: 'bar',
              data: data.searchCnt.gen
            }
            let cancerInfo = {
              name: 'CANCER',
              type: 'bar',
              data: data.searchCnt.cancer
            }
            let immuneCell = {
              name: 'IMMUNE CELL',
              type: 'bar',
              data: data.searchCnt.immune_cell
            }
            let immunePath = {
              name: 'IMMUNE PATHWAY',
              type: 'bar',
              data: data.searchCnt.immune_pathway
            }
            option.series = []
            option.series.push(genInfo)
            option.series.push(cancerInfo)
            option.series.push(immuneCell)
            option.series.push(immunePath)

            this.chartBar = echarts.init(document.getElementById('J_chartBarBox'))
            this.chartBar.setOption(option)
            window.addEventListener('resize', () => {
              this.chartBar.resize()
            })
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
            debugger
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

            console.log('--------------------------------')
            console.log(chartPie)
            window.addEventListener('resize', () => {
              this.chartPie.resize()
            })
            debugger

            console.log(option.series.data)
          } else {
            this.$message.error(data.msg)
          }
        })
      },

      // 饼状图
      initChartPie2 () {
        var chartPie = echarts.init(document.getElementById('J_chartPieBox2'))

        var option = {
          backgroundColor: '#ffffff',
          title: {
            text: 'GENE TYPE',
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
          url: this.$http.adornUrl('/web/statistic/getCntByCol/gene_type'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            debugger
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

            console.log('--------------------------------')
            console.log(chartPie)
            window.addEventListener('resize', () => {
              this.chartPie.resize()
            })
            debugger

            console.log(option.series.data)
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      // 散点图
      initChartScatter () {
        var option = {
          backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [
            { offset: 0, color: '#f7f8fa' },
            { offset: 1, color: '#cdd0d5' }
          ]),
          title: {
            text: '1990 与 2015 年各国家人均寿命与 GDP'
          },
          legend: {
            right: 10,
            data: ['1990', '2015']
          },
          xAxis: {
            splitLine: {
              lineStyle: {
                type: 'dashed'
              }
            }
          },
          yAxis: {
            splitLine: {
              lineStyle: {
                type: 'dashed'
              }
            },
            scale: true
          },
          series: [
            {
              name: '1990',
              data: [
                [28604, 77, 17096869, 'Australia', 1990],
                [31163, 77.4, 27662440, 'Canada', 1990],
                [1516, 68, 1154605773, 'China', 1990],
                [13670, 74.7, 10582082, 'Cuba', 1990],
                [28599, 75, 4986705, 'Finland', 1990],
                [29476, 77.1, 56943299, 'France', 1990],
                [31476, 75.4, 78958237, 'Germany', 1990],
                [28666, 78.1, 254830, 'Iceland', 1990],
                [1777, 57.7, 870601776, 'India', 1990],
                [29550, 79.1, 122249285, 'Japan', 1990],
                [2076, 67.9, 20194354, 'North Korea', 1990],
                [12087, 72, 42972254, 'South Korea', 1990],
                [24021, 75.4, 3397534, 'New Zealand', 1990],
                [43296, 76.8, 4240375, 'Norway', 1990],
                [10088, 70.8, 38195258, 'Poland', 1990],
                [19349, 69.6, 147568552, 'Russia', 1990],
                [10670, 67.3, 53994605, 'Turkey', 1990],
                [26424, 75.7, 57110117, 'United Kingdom', 1990],
                [37062, 75.4, 252847810, 'United States', 1990]
              ],
              type: 'scatter',
              symbolSize: function (data) {
                return Math.sqrt(data[2]) / 5e2
              },
              label: {
                emphasis: {
                  show: true,
                  formatter: function (param) {
                    return param.data[3]
                  },
                  position: 'top'
                }
              },
              itemStyle: {
                normal: {
                  shadowBlur: 10,
                  shadowColor: 'rgba(120, 36, 50, 0.5)',
                  shadowOffsetY: 5,
                  color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                    { offset: 0, color: 'rgb(251, 118, 123)' },
                    { offset: 1, color: 'rgb(204, 46, 72)' }
                  ])
                }
              }
            },
            {
              name: '2015',
              data: [
                [44056, 81.8, 23968973, 'Australia', 2015],
                [43294, 81.7, 35939927, 'Canada', 2015],
                [13334, 76.9, 1376048943, 'China', 2015],
                [21291, 78.5, 11389562, 'Cuba', 2015],
                [38923, 80.8, 5503457, 'Finland', 2015],
                [37599, 81.9, 64395345, 'France', 2015],
                [44053, 81.1, 80688545, 'Germany', 2015],
                [42182, 82.8, 329425, 'Iceland', 2015],
                [5903, 66.8, 1311050527, 'India', 2015],
                [36162, 83.5, 126573481, 'Japan', 2015],
                [1390, 71.4, 25155317, 'North Korea', 2015],
                [34644, 80.7, 50293439, 'South Korea', 2015],
                [34186, 80.6, 4528526, 'New Zealand', 2015],
                [64304, 81.6, 5210967, 'Norway', 2015],
                [24787, 77.3, 38611794, 'Poland', 2015],
                [23038, 73.13, 143456918, 'Russia', 2015],
                [19360, 76.5, 78665830, 'Turkey', 2015],
                [38225, 81.4, 64715810, 'United Kingdom', 2015],
                [53354, 79.1, 321773631, 'United States', 2015]
              ],
              type: 'scatter',
              symbolSize: function (data) {
                return Math.sqrt(data[2]) / 5e2
              },
              label: {
                emphasis: {
                  show: true,
                  formatter: function (param) {
                    return param.data[3]
                  },
                  position: 'top'
                }
              },
              itemStyle: {
                normal: {
                  shadowBlur: 10,
                  shadowColor: 'rgba(25, 100, 150, 0.5)',
                  shadowOffsetY: 5,
                  color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                    { offset: 0, color: 'rgb(129, 227, 238)' },
                    { offset: 1, color: 'rgb(25, 183, 207)' }
                  ])
                }
              }
            }
          ]
        }
        this.chartPie = echarts.init(document.getElementById('J_chartScatterBox'))
        this.chartPie.setOption(option)
        window.addEventListener('resize', () => {
          this.chartPie.resize()
        })
      }
    }
  }
</script>

<style lang="scss">
  .mod-demo-echarts {
    > .el-alert {
      margin-bottom: 10px;
    }
    > .el-row {
      margin-top: -10px;
      margin-bottom: -10px;
      .el-col {
        padding-top: 10px;
        padding-bottom: 10px;
      }
    }
    .chart-box {
      min-height: 360px;
      width: 100%;
    }
  }
  .circle-cust {
    position: relative;
    top: 28px;
    left: -100%;
  }
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }

  .mod-demo-echarts{
    background-color: #ebeff4;
    min-height: 600px;
    height: 100%;
  }
  .statistInfo{
    background-color: #ffffff;
    width: 100%;
    padding: 20px 0;
    margin-bottom: 20px;
    border-top: 1px solid #e5e5e5;
    -moz-box-shadow:0px 2px 5px #ccc;
    -webkit-box-shadow:0px 2px 5px #ccc;
    box-shadow:0px 2px 5px #ccc;
  }

  .statistInfo_c{
    width: 1200px;
    margin: 0 auto;
    overflow: hidden;
  }
  .statisInfo_l{
    background-color: #00b7ee;
    width: 240px;
    height: 120px;
    float: left;
  }
  .statisInfo_r{
    width: 930px;
    float: right;
  }
  .statisInfo_r_til{
    font-weight: 700;
    font-size: 28px;
  }
  .statisInfo_r_txt{
    padding: 0 !important;
    font-size: 16px;
    line-height: 24px;
    color: #646464;
  }
  .statist_cnt{
    width: 1200px;
    margin: 0 auto;
  }
  .statist_bar{
    overflow: hidden;
  }
  .statist_bar_box{
    width: 580px;
  }
  .bar_cnt{
    width: 100%;
    height: 360px;
    margin-top: 20px;
    background-color: #ffffff;
  }
  .chart_til{
    font-weight: 600;
    font-size: 16px;
  }
  .graphBox{
    margin-top: 20px;
    background-color: #ffffff;
    width: 100%;
    height: 320px;
    box-sizing: border-box;
    padding: 10px;
  }
</style>
