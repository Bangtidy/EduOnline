package com.bangtidy.educenter.service;

import com.bangtidy.educenter.entity.UcenterMember;
import com.bangtidy.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-04-24
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录方法
    String login(UcenterMember member);

    //注册方法
    Boolean register(RegisterVo registerVo);
}
