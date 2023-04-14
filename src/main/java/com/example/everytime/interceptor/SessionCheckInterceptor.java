package com.example.everytime.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false); // 세션 가져오기 (세션이 없으면 null 반환)

        if (session != null && !session.isNew()) { // 세션이 있는 경우
            long lastAccessedTime = session.getLastAccessedTime(); // 세션의 마지막 접근 시간
            long currentTime = System.currentTimeMillis(); // 현재 시간

            // 세션의 유효 시간(10분)을 체크해서 만료된 경우
            if (currentTime - lastAccessedTime > 600000) {
                session.invalidate(); // 세션 만료
                response.sendRedirect("/login");
                return false;
            }
            return true;
        }
        return false;
    }
}
