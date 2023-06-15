package com.example.everytime.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false); // 현재 요청의 세션이 없으면 null을 반환
        if (session != null && !session.isNew()) { // 세션이 존재하면서 새로 생성된 세션이 아닌 경우
            long lastAccessTime = session.getLastAccessedTime(); // 세션의 마지막 접근 시간을 가져옴
            long currentTime = System.currentTimeMillis(); // 현재 시간을 가져옴
            int sessionTimeout = session.getMaxInactiveInterval(); // 세션의 만료 시간을 가져옴

            if (currentTime - lastAccessTime > sessionTimeout * 1000L) { // 마지막 접근 시간부터 세션의 만료 시간을 초과한 경우
                session.invalidate(); // 세션을 무효화시킴
                response.sendError(401,"유효하지 않은 세션입니다.");
            }
        }else if(session == null){
            //session이 존재하지 않는 경우 login이 필요하다고 함
            response.sendError(401,"세션이 존재하지 않습니다.");
        }
        return true;
    }

}
