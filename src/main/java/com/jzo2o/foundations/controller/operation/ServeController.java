package com.jzo2o.foundations.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 批量添加区域服务
     * @param serveUpsertReqDTOList
     */
    @PostMapping("/batch")
    @ApiOperation("批量添加区域服务")
    public void batch(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        serveService.batchAdd(serveUpsertReqDTOList);
    }

    /**
     * 区域服务价格修改
     * @param id
     * @param price
     */
    @PutMapping("/{id}")
    @ApiOperation("区域服务价格修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void update( @PathVariable("id") Long id,
                        @RequestParam("price") BigDecimal price) {
        serveService.update(id, price);
    }

    /**
     * 区域服务下架
     * @param id 服务id
     */
    @PutMapping("/onSale/{id}")
    @ApiOperation("区域服务上架")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void onSale(@PathVariable("id") Long id) {
        serveService.onSale(id);
    }

    /**
     * 区域服务删除
     * @param id
     */
    @DeleteMapping("/{id}")
    @ApiOperation("区域服务删除")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void delete(@PathVariable("id") Long id) {
        serveService.delete(id);
    }

    /**
     * 区域服务下架
     * @param id 服务id
     */
    @PutMapping("/offSale/{id}")
    @ApiOperation("区域服务下架")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void offSale(@PathVariable("id") Long id) {
        serveService.offSale(id);
    }

    /**
     * 区域服务设置热门
     * @param id 服务id
     */
    @PutMapping("/onHot/{id}")
    @ApiOperation("区域服务设置热门")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void onHot(@PathVariable("id") Long id) {
        serveService.onHot(id);
    }

    /**
     * 区域服务取消热门
     * @param id 服务id
     */
    @PutMapping("/offHot/{id}")
    @ApiOperation("区域服务取消热门")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void offHot(@PathVariable("id") Long id) {
        serveService.offHot(id);
    }
}
