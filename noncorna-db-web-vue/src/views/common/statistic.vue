<template>
  <div class="mod-demo-echarts">
    <div class="statistInfo">
      <div class="statistInfo_c">
        <div class="statisInfo_l">
          <img src="../../assets/img/pic1.jpg" />
        </div>
        <div class="statisInfo_r">
          <div class="statisInfo_r_til">Welcome to ImmncRNA</div>
          <p class="statisInfo_r_Welcome to NoncoRNAtxt">Immncrna is a set of specialist databases related to the study of the relationship among tumors, immunity, and ncRNAs. This study provides a database resource for future studies of tumors and immune-related ncRNAs. Each entry contains detailed information on references, PMID, gene symbol, gene type, cancer type, tissue origin, organism, target gene, downstream effect, function, immune cells and other information. This database includes the following features and data: links between immune-related ncRNAs and target genes; proofs between immune-related ncRNAs and functions; lists of ncRNA-related immune cells and immune pathway. We believe it could be severed as a valuable resource for understanding the functions of ncRNAs, especially immune-related ncRNAs, and advancing the development of immunotherapy</p>
        </div>
      </div>
    </div>
    <div class="statist_cnt">
        <div class="statist_bar">
          <div class="statist_bar_box" style="float: left">
            <div class="chart_til">DATABASE SEARCH COUNT BEFORE 7 DAYS</div>
            <div class="graphBox" style="height: 380px;">
              <div id="J_chartBarBox" class="chart-box"></div>
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
          <div class="chart_til">CANCER TYPE</div>
          <div class="bar_cnt">
            <div id="J_chartPieBox" class="chart-box"></div>
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
            'data': [ ]
          },
          'yAxis': {
            'type': 'value'
          },
          'series': []
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

      // 饼状图
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
                lineStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                },
                smooth: 0.2,
                length: 10,
                length2: 20
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
