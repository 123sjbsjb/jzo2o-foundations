package com.jzo2o.foundations.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author SJB
 * @author 1.0
 * @description 区域服务管理相关接口
 * @date 2024/6/6 19:56
 */
@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务管理相关接口")
public class ServeController {
    @Resource
    private IServeService serveService;

    /**
     * 分页查询区域服务列表
     * @param servePageQueryReqDTO 分页查询区域服务列表请求参数
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询区域服务列表")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        return serveService.page(servePageQueryReqDTO);
    }
}
