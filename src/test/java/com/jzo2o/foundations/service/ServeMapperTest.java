package com.jzo2o.foundations.service;

import cn.hutool.core.lang.Assert;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author SJB
 * @author 1.0
 * @description ServeMapperTest的测试类
 * @date 2024/6/6 21:17
 */
@SpringBootTest
@Slf4j
public class ServeMapperTest {
    @Resource
    private ServeMapper serveMapper;

    @Test
    public void testQueryServeListByRegionId() {
        List<ServeResDTO> serveResDTOS = serveMapper.queryServeListByRegionId(1686303222843662337L);
        Assert.notEmpty(serveResDTOS, "查询结果为空");
    }
}
