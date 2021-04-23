package com.bangtidy.educms.controller;


import com.bangtidy.commonutils.R;
import com.bangtidy.educms.entity.CrmBanner;
import com.bangtidy.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
public class BannerFrontController {

    @Autowired
    private CrmBannerService crmBannerService;

    //查询所有banner
    @ApiOperation(value = "获取全部Banner")
    @GetMapping("getAll")
    public R getAllBanner(){
        List<CrmBanner> list = crmBannerService.selectAllBanner();
        return R.ok().data("list",list);
    }

}

