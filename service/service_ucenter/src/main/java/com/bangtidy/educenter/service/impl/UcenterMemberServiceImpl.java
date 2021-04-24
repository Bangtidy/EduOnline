package com.bangtidy.educenter.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.bangtidy.commonutils.JwtUtils;
import com.bangtidy.commonutils.MD5;
import com.bangtidy.educenter.entity.UcenterMember;
import com.bangtidy.educenter.entity.vo.RegisterVo;
import com.bangtidy.educenter.mapper.UcenterMemberMapper;
import com.bangtidy.educenter.service.UcenterMemberService;
import com.bangtidy.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-24
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断查询对象是否为空
        if(mobileMember == null){
            //没有这个手机号
            throw new GuliException(20001,"登录失败");
        }

        //判断密码
        //对用户输入的密码加密（MD5），后与数据库密码比较
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())){
            throw new GuliException(20001,"登录失败");
        }

        //判断用户是否禁用
        if(mobileMember.getIsDeleted()){
            throw new GuliException(20001,"登录失败");
        }

        //登录成功
        String token = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return token;
    }

    //注册
    @Override
    public Boolean register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();//手机号
        String nickname = registerVo.getNickname();//昵称
        String password = registerVo.getPassword();//密码

        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
            || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)){
            throw new GuliException(20001,"注册失败");
        }

        //判断验证码是否正确
        //获取redis中的验证码
        String redisCode  = redisTemplate.opsForValue().get(mobile);
        if(StringUtils.isEmpty(redisCode)){
            throw new GuliException(20001,"验证码已过期");
        }
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"验证码错误");
        }

        //判断手机号是否重复，表里已经存在相同手机号不进行添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new GuliException(20001,"该手机已经注册过 ");
        }

        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);//用户不禁用
        member.setAvatar("https://bangtidy-edu.oss-cn-guangzhou.aliyuncs.com/3ca5067d-3214-47d6-a52f-bf6410a73c0b.png");//默认头像
        baseMapper.insert(member);

        return true;
    }
}
