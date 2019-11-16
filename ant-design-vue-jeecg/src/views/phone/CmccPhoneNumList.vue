<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="手机号">
              <a-input placeholder="请输入手机号" v-model="queryParam.phoneName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="是否被选">
              <j-dict-select-tag placeholder="请选择是否被选" v-model="queryParam.isUse" dictCode="phone_status"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col v-has="'online:cmcc_phone_num:add'" :md="6" :sm="8">
              <a-form-item label="代理商">
                <j-select-user-by-dep v-model="queryParam.sysUserCode" :trigger-change="true"/>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="16">
              <a-form-item label="更新时间">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.updateTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.updateTime_end"></j-date>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div v-has="'online:cmcc_phone_num:add'" class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('待选号码')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="tableScroll"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a v-has="'online:cmcc_phone_num:add'" @click="handleEdit(record)">编辑</a>
          <a v-has="'online:cmcc_phone_num:phone_select'" id="phone_select" @click="phone_select(record)">选号</a>
          <a-divider v-has="'online:cmcc_phone_num:add'" type="vertical" />
          <a-dropdown v-has="'online:cmcc_phone_num:add'">
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <cmccPhoneNum-modal ref="modalForm" @ok="modalFormOk"></cmccPhoneNum-modal>
  </a-card>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CmccPhoneNumModal from './modules/CmccPhoneNumModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import {initDictOptions, filterMultiDictText, filterDictText} from '@/components/dict/JDictSelectUtil'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import { colAuthFilter } from "@/utils/authFilter"

  export default {
    name: "CmccPhoneNumList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      CmccPhoneNumModal,
      JSelectUserByDep
    },
    data () {
      return {
        description: '待选号码管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'手机号',
            align:"center",
            dataIndex: 'phoneName'
          },
          {
            title:'是否被选',
            align:"center",
            dataIndex: 'isUse',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isUse'], text+"")
              }
            }
          },
          {
            title:'代理商',
            align:"center",
            dataIndex: 'sysUserCode'
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/phone/cmccPhoneNum/list",
          delete: "/phone/cmccPhoneNum/delete",
          deleteBatch: "/phone/cmccPhoneNum/deleteBatch",
          exportXlsUrl: "/phone/cmccPhoneNum/exportXls",
          importExcelUrl: "phone/cmccPhoneNum/importExcel",
        },
        dictOptions:{
         isUse:[],
        },
        tableScroll:{x :4*147+50}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      phone_select(row){
        console.log(this.$store.getters.userInfo.id, row)
        const that = this;
        let sysUserCode = this.$store.getters.userInfo.username
        
        // 判断是否可以操作
        if (row.isUse == '1'){
          that.$message.warning('号码'+row.phoneName+'已经绑定');
          return
        }

        // 找出代理商信息，判断是否可操作：
        // TODO: 1、判断是否超量，2、判断是否在选号时间范围内
        that.confirmLoading = true;
        let httpurl = '/phone/agentConfig/list';
        let method = 'get';
        let formData = { params: { sysUserCode }};
        console.log("表单提交数据",formData)
        httpAction(httpurl,formData,method).then((res)=>{
          if(res.success && res.result.total > 0){
            // 更新号码数据
            formData = { id: row.id, isUse: "1", sysUserCode: sysUserCode };
            console.log("表单提交数据",formData)
            return httpAction('/phone/cmccPhoneNum/phone_select',formData,'put')
          }else{
            that.$message.warning(res.message);
          }
        }).then((res)=>{
          if(res.success){
            // 更新界面
            row.sysUserCode = sysUserCode
            row.isUse = '1'
            that.$message.success(res.message);
          }else{
            that.$message.warning(res.message);
          }
        })
        .finally(() => {
          that.confirmLoading = false;
          that.searchQuery();
        })
      },
      initDictConfig(){
        initDictOptions('phone_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isUse', res.result)
          }
        })
      }
    },
    created() {
      this.columns = colAuthFilter(this.columns,'cmccPhoneNum:');
      this.loadData();
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>