package org.jeecg.modules.phone.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.phone.entity.CmccPhoneNum;
import org.jeecg.modules.phone.service.ICmccPhoneNumService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Description: 待选号码
 * @Author: jeecg-boot
 * @Date:   2019-11-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/phone/cmccPhoneNum")
@Slf4j
public class CmccPhoneNumController extends JeecgController<CmccPhoneNum, ICmccPhoneNumService> {
	@Autowired
	private ICmccPhoneNumService cmccPhoneNumService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cmccPhoneNum
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	@PermissionData
	public Result<?> queryPageList(CmccPhoneNum cmccPhoneNum,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CmccPhoneNum> queryWrapper = QueryGenerator.initQueryWrapper(cmccPhoneNum, req.getParameterMap());
		Page<CmccPhoneNum> page = new Page<CmccPhoneNum>(pageNo, pageSize);
		IPage<CmccPhoneNum> pageList = cmccPhoneNumService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 分页列表查询
	 *
	 * @param cmccPhoneNum
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/my")
	@PermissionData
	public Result<?> myList(CmccPhoneNum cmccPhoneNum,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CmccPhoneNum> queryWrapper = QueryGenerator.initQueryWrapper(cmccPhoneNum, req.getParameterMap());
		Page<CmccPhoneNum> page = new Page<CmccPhoneNum>(pageNo, pageSize);
		IPage<CmccPhoneNum> pageList = cmccPhoneNumService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cmccPhoneNum
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CmccPhoneNum cmccPhoneNum) {
		cmccPhoneNumService.save(cmccPhoneNum);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cmccPhoneNum
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CmccPhoneNum cmccPhoneNum) {
		cmccPhoneNumService.updateById(cmccPhoneNum);
		return Result.ok("编辑成功!");
	}

	//online:cmcc_phone_num:phone_select
	/**
	 *  编辑
	 *
	 * @param cmccPhoneNum
	 * @return
	 */
	@PutMapping(value = "/phone_select")
	@RequiresPermissions("online:cmcc_phone_num:phone_select")
	public Result<?> selectPhone(@RequestBody CmccPhoneNum cmccPhoneNum) {
		cmccPhoneNumService.updateById(cmccPhoneNum);
		return Result.ok("选号成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		cmccPhoneNumService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cmccPhoneNumService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CmccPhoneNum cmccPhoneNum = cmccPhoneNumService.getById(id);
		if(cmccPhoneNum==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(cmccPhoneNum);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cmccPhoneNum
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CmccPhoneNum cmccPhoneNum) {
        return super.exportXls(request, cmccPhoneNum, CmccPhoneNum.class, "待选号码");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CmccPhoneNum.class);
    }

}
