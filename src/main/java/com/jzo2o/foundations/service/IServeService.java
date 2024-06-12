package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author sjb
 * @since 2024-06-06
 */
public interface IServeService extends IService<Serve> {
    /**
     * 分页查询区域服务列表
     * @param servePageQueryReqDTO
     * @return
     */
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    /**
     * 批量添加区域服务
     * @param serveUpsertReqDTOList
     */
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 区域服务价格修改
     * @param id
     * @param price
     * @return
     */
    Serve update(Long id, BigDecimal price);

    /**
     * 区域服务上架
     * @param id
     */
    Serve onSale(Long id);

    /**
     * 区域服务删除
     * @param id
     */
    void delete(Long id);

    /**
     * 区域服务下架
     * @param id
     */
    void offSale(Long id);

    Serve onHot(Long id);

    Serve offHot(Long id);
}
