<template>
  <div class="mod-home">
    <div class="banner">
      <div class="ban_searcher">
        <div class="banSer_c">
          <div class="ban_til">Welcome to NoncoRNA</div>
          <div class="searcher_box">
            <el-input
                      v-model="keywords"
                      placeholder="keywords"
                      prefix-icon="el-icon-search"
                      style="width: 88%;">
            </el-input>
            <el-button type="primary" @click="search()" style="height:50px;border-radius: 0;">Search</el-button>
          </div>
        </div>
      </div>
    </div>
    <div class="index_main">
      <!--  介绍    -->
      <div class="welcome">
        <div class="welcome_pic">
          <img src="../../assets/img/pic.jpg" />
        </div>
        <div class="weclome_js">
          <div class="welcome_til">
            <p>Welcome to NoncoRNA</p>
            <a href="#">Learn More+</a>
          </div>
          <p class="welcome_txt" v-html="webconfig.description"></p>
        </div>

      </div>
      <!--      -->
      <div class="index_bt">
        <!--    Statistics      -->
        <div class="statisticsMain">
          <div class="quickTil">Statistics</div>
          <div>
            <div class="statistBox1 statistBox">
              <p>cancer type:</p>
              <span>{{ cancerTypeCnt }}</span>
            </div>
            <div class="statistBox2 statistBox">
              <p>gene id:</p>
              <span>{{ genCnt }}</span>
            </div>
            <div class="statistBox1 statistBox">
              <p>immune cell:</p>
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
                <a target="_blank" :href="news.linkUrl">
                  <p>{{ news.title }}</p>
                  <span>{{ news.ct }}</span>
                </a>
              </li>
            </ul>
          </div>
        </div>
        <!--    searcher    -->
        <div class="quickSerMain">
          <div class="quickTil">Quick Search</div>
          <svg :width='width' :height='height' @mousemove='listener($event)'>
            <a :href="tag.href" v-for='tag in tags' >
              <text :x='tag.x' :y='tag.y':fill="tag.fill" :font-size='15 * (600/(600-tag.z))' :fill-opacity='((400+tag.z)/600)'>{{tag.text}}</text>
            </a>
          </svg>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
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
    /* created: function () {
      let tags = []
      for (let i = 0; i < this.tagsNum; i++) {
        let tag = {}
        let k = -1 + (2 * (i + 1) - 1) / this.tagsNum
        let a = Math.acos(k)
        let b = a * Math.sqrt(this.tagsNum * Math.PI)
        tag.text = i + 'tag'
        tag.x = this.CX + this.RADIUS * Math.sin(a) * Math.cos(b)
        tag.y = this.CY + this.RADIUS * Math.sin(a) * Math.sin(b)
        tag.z = this.RADIUS * Math.cos(a)
        tag.href = 'https://imgss.github.io'
        tags.push(tag)
      }

      this.tags = tags
    }, */
    mounted () { // 使球开始旋转
      setInterval(() => {
        this.rotateX(this.speedX)
        this.rotateY(this.speedY)
      }, 30)
    },
    activated () {
      this.getWebConfig()
      this.getTopNews()
      this.getStatisticsBaseInfo()
      this.getTags()
    },
    methods: {
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
        this.$router.push({path: '/search-result', query: { 'keyWords': 1 }})
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
              tag.href = '#/search-result?keyWords=' + tagsTemp[i].text
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
      }
    }
  }
</script>

<style scoped>
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
    overflow: hidden;
    font-weight: 600;
    height: 92px;
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
    background-color: #738ca5;
  }
  .statistBox2{
    background-color: #114694;
  }
  .statistBox p{
    float: left;
    width: 180px;
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

