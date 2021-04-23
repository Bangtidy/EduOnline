package com.bangtidy.educms.service;

import com.bangtidy.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-22
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //查询所有的banner
    List<CrmBanner> selectAllBanner();

}
