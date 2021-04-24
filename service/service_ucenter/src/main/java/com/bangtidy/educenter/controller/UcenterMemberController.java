package com.bangtidy.educenter.controller;


import com.bangtidy.commonutils.JwtUtils;
import com.bangtidy.commonutils.R;
import com.bangtidy.educenter.entity.UcenterMember;
import com.bangtidy.educenter.entity.vo.RegisterVo;
import com.bangtidy.educenter.service.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        //menmber对象包含手机号和密码
        //调用service实现单点登录
        //调用JWT生成token,返回token值
        String  token = memberService.login(member);
        return R.ok().data("token",token);
    }


    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        Boolean flag = memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //根据id查找用户
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

}

