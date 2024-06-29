package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;

import java.util.List;

/**
 * @author SJB
 * @author 1.0
 * @description TODO
 * @date 2024/6/14 20:50
 */
public interface ServeAggregationService {
    /**
     * 查询服务列表
     *
     * @param cityCode    城市编码
     * @param serveTypeId 服务类型id
     * @param keyword     关键词
     * @return 服务列表
     */
    List<ServeSimpleResDTO> findServeList(String cityCode, Long serveTypeId, String keyword);
}
