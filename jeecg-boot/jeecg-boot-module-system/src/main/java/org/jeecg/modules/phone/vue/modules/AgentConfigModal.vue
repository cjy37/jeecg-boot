<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="代理商" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-user-by-dep v-decorator="['sysUserCode']" :trigger-change="true"/>
        </a-form-item>
        <a-form-item label="优先级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'acLevel', validatorRules.acLevel]" placeholder="请输入优先级" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="可选号码数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'acPhoneNum', validatorRules.acPhoneNum]" placeholder="请输入可选号码数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="选号开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择选号开始时间" v-decorator="[ 'acStartTime', validatorRules.acStartTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="选号结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择选号结束时间" v-decorator="[ 'acEndTime', validatorRules.acEndTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'  
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'

  export default {
    name: "AgentConfigModal",
    components: { 
      JDate,
      JSelectUserByDep,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules:{
        sysUserCode:{},
        acLevel:{},
        acPhoneNum:{},
        acStartTime:{},
        acEndTime:{},
        },
        url: {
          add: "/phone/agentConfig/add",
          edit: "/phone/agentConfig/edit",
        }
     
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sysUserCode','acLevel','acPhoneNum','acStartTime','acEndTime'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'sysUserCode','acLevel','acPhoneNum','acStartTime','acEndTime'))
      },

      
    }
  }
</script>