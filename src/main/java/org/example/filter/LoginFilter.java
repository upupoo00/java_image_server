package org.example.filter;

import org.example.util.Util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户会话的统一管理：基于过滤器实现
 * 可以统一的进行权限管理 。 过滤敏感信息等
 *
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //获取请求路径，判断是否为敏感资源，如果是，需要校验session
        String url = req.getServletPath();//应用上下文路径后面的服务路径
        //前端敏感资源为 /index.html 后端敏感资源为 /image  /imageShow
        if(url.equals("/index.html") && !isLogin(req)){
            //前端敏感资源，重定向到登录页面
            //真实的代码需要写绝对路径（通过request对象 获取绝对路径的每个部分）
//            req.getScheme();//http
//            req.getServerName();//ip或域名
//            req.getServerPort();//port
//            req.getContextPath();//应用上下文路径
            resp.sendRedirect("login.html");
            return;
        }else if((url.equals("/image") || url.equals("/imageShow")) && !isLogin(req)){
            //后端敏感资源，设置401响应状态码，返回json数据
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/json");
            resp.setStatus(401);
            Map<String,Object> map = new HashMap<>();
            map.put("ok",false);
            map.put("msg","用户未登录，不允许访问");
            String json = Util.serialize(map);
            resp.getWriter().println(json);
            return;
        }
        //前后端敏感资源，但已登录，或开放的资源，允许访问
        chain.doFilter(request, response);
    }

    public static boolean isLogin(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if(session!=null){
            //获取的键为登录时保存在Session中的键
            Object username = session.getAttribute("username");
            if(username!=null){
                //已登录用户访问
                return true;
            }
        }
        //未登录用户访问
        return false;
    }

    @Override
    public void destroy() {

    }
}
