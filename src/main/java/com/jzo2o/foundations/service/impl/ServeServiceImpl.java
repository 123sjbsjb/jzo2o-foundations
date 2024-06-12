package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author sjb
 * @since 2024-06-06
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {
    /**
     * 分页查询区域服务列表
     * @param servePageQueryReqDTO
     * @return
     */
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        PageResult<ServeResDTO> serveResDTOPageResult = PageHelperUtils.selectPage(servePageQueryReqDTO,
                () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
        return serveResDTOPageResult;
    }

    @Resource
    private ServeItemMapper serveItemMapper;
    @Resource
    private RegionMapper regionMapper;

    /**
     * 批量添加区域服务
     * @param serveUpsertReqDTOList
     */
    @Override
    @Transactional
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            //1.校验服务项是否为启用状态，不是启用状态不能新增
            ServeItem serveItem = serveItemMapper.selectById(serveUpsertReqDTO.getServeItemId());
            //如果服务项信息不存在或未启用
            if(ObjectUtil.isNull(serveItem) || serveItem.getActiveStatus()!= FoundationStatusEnum.ENABLE.getStatus()){
                throw new ForbiddenOperationException("该服务未启用无法添加到区域下使用");
            }

            //2.校验是否重复新增
            Integer count = lambdaQuery()
                    .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId())
                    .eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId())
                    .count();
            if(count>0){
                throw new ForbiddenOperationException(serveItem.getName()+"服务已存在");
            }

            //3.新增服务
            Serve serve = BeanUtil.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);
        }
    }

    /**
     * 区域价格修改
     * @param id
     * @param price
     * @return
     */
    @Override
    @Transactional
    public Serve update(Long id, BigDecimal price) {
        //1.更新服务价格
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getPrice, price)
                .update();
        if(!update){
            throw new CommonException("修改服务价格失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 区域服务上架
     * @param id
     */
    @Override
    @Transactional
    public Serve onSale(Long id) {
        //1.校验服务是否存在
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //2.校验服务状态是否为草稿或下架状态
        if(serve.getSaleStatus()!= FoundationStatusEnum.INIT.getStatus() && serve.getSaleStatus()!= FoundationStatusEnum.DISABLE.getStatus()){
            throw new ForbiddenOperationException("服务状态不为草稿或下架状态,无法上架");
        }
        //3.校验服务项是否是否存在
        ServeItem serveItem = serveItemMapper.selectById(serve.getServeItemId());
        if(ObjectUtil.isNull(serveItem)){
            throw new ForbiddenOperationException("服务项不存在");
        }
        //4.校验服务项是否启用
        if(serveItem.getActiveStatus()!= FoundationStatusEnum.ENABLE.getStatus()){
            throw new ForbiddenOperationException("本服务所属的服务项未启用,无法上架");
        }
        //5.更新服务状态为上架
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
                .update();
        if(!update){
            throw new CommonException("服务上架失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 区域服务删除
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        //1.校验服务是否存在
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //2.校验服务状态是否为草稿状态
        if(serve.getSaleStatus()!= FoundationStatusEnum.INIT.getStatus()){
            throw new ForbiddenOperationException("服务状态不为草稿状态,无法删除");
        }
        //3.删除服务
        baseMapper.deleteById(id);
    }

    /**
     * 区域服务下架
     * @param id
     */
    @Override
    @Transactional
    public void offSale(Long id) {
        //1.校验服务是否存在
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //2.校验服务状态是否为上架状态
        if(serve.getSaleStatus()!= FoundationStatusEnum.ENABLE.getStatus()){
            throw new ForbiddenOperationException("服务状态不为上架状态,无法下架");
        }
        //3.更新服务状态为下架
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus())
                .update();
        if(!update){
            throw new CommonException("服务下架失败");
        }

    }

    @Override
    @Transactional
    public Serve onHot(Long id) {
        //1.校验服务是否存在
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //2.校验服务状态是否为上架状态
        if(serve.getSaleStatus()!= FoundationStatusEnum.ENABLE.getStatus()){
            throw new ForbiddenOperationException("服务状态不为上架状态,无法设置热门");
        }
        //3.更新服务状态为热门
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getIsHot, FoundationStatusEnum.ONHOT.getStatus())
                .update();
        if(!update){
            throw new CommonException("服务设置热门失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 区域服务取消热门
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Serve offHot(Long id) {
        //1.校验服务是否存在
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //2.校验服务状态是否为上架状态
        if(serve.getSaleStatus()!= FoundationStatusEnum.ENABLE.getStatus()){
            throw new ForbiddenOperationException("服务状态不为上架状态,无法取消热门");
        }
        //3.更新服务状态为非热门
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getIsHot, FoundationStatusEnum.OFFHOT.getStatus())
                .update();
        if(!update){
            throw new CommonException("服务取消热门失败");
        }
        return baseMapper.selectById(id);
    }
}
