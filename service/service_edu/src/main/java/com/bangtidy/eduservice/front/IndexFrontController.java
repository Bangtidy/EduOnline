package com.bangtidy.eduservice.front;

import com.bangtidy.commonutils.R;
import com.bangtidy.eduservice.entity.EduCourse;
import com.bangtidy.eduservice.entity.EduTeacher;
import com.bangtidy.eduservice.service.EduCourseService;
import com.bangtidy.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    //查询前8条热门课程，查询前4个老师
    //TODO 引入redis缓存
    @GetMapping("index")
    @Cacheable
    public R index(){
        //查询前8条热门课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> eduList = courseService.list(wrapper);

        //查询前4个名师
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);

        return R.ok().data("eduList",eduList).data("teacherList",teacherList);
    }
}
