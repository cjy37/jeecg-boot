package org.jeecg.modules.phone.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 待选号码
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
@Data
@TableName("cmcc_phone_num")
public class CmccPhoneNum implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    private java.lang.String phoneName;
	/**是否被选*/
	@Excel(name = "是否被选", width = 15)
    private java.lang.String isUse;
	/**代理商*/
	@Excel(name = "代理商", width = 15)
    private java.lang.String sysUserCode;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    private java.lang.String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date updateTime;
	/**更新人ID*/
	@Excel(name = "更新人ID", width = 15)
    private java.lang.String updateBy;
}
