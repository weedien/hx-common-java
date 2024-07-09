package cn.weedien.component.common.filter;

import cn.weedien.component.common.auth.UserContext;
import cn.weedien.component.common.auth.UserInfoDTO;
import cn.weedien.component.common.constant.UserConstant;
import cn.weedien.component.common.toolkit.StringUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UserTransmitFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String userId = httpServletRequest.getHeader(UserConstant.USER_ID_KEY);
        if (StringUtil.hasText(userId)) {
            String userName = httpServletRequest.getHeader(UserConstant.USER_NAME_KEY);
            String realName = httpServletRequest.getHeader(UserConstant.REAL_NAME_KEY);
            if (StringUtil.hasText(userName)) {
                userName = URLDecoder.decode(userName, UTF_8);
            }
            if (StringUtil.hasText(realName)) {
                realName = URLDecoder.decode(realName, UTF_8);
            }
            String token = httpServletRequest.getHeader(UserConstant.USER_TOKEN_KEY);
            UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .username(userName)
                    .realName(realName)
                    .token(token)
                    .build();
            UserContext.setUser(userInfoDTO);
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}