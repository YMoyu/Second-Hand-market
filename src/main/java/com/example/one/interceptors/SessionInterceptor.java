package com.example.one.interceptors;

import com.example.one.Service.NotificationService;
import com.example.one.mapper.NotificationMapper;
import com.example.one.mapper.UserMapper;
import com.example.one.model.User;
import com.example.one.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user=null;
        Cookie[] cookies = request.getCookies();


        if(cookies!=null&&cookies.length!=0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    UserExample userExample =new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                   List<User> users = userMapper.selectByExample(userExample);
//
                    if(users.size()!=0){

                        request.getSession().setAttribute("user",users.get(0));
                        Long unReadCount = notificationService.unReadCount(users.get(0).getId());
                        request.getSession().setAttribute("unReadCount",unReadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
