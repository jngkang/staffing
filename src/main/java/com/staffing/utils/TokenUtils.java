package com.staffing.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.staffing.entity.Employee;
import com.staffing.service.IEmployeeService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author JngKang
 * @date 2022-03-02 11:33
 * @description JWT Token工具类  用来生成token
 */
public class TokenUtils {

    private static IEmployeeService staticEmployeeService;

    @Resource
    private IEmployeeService employeeService;

    @PostConstruct
    public void setUserService() {
        staticEmployeeService = employeeService;
    }

    public static String genToken(String userId, String sign) {
        // 将userId保存到token里面,作为负荷
        return JWT.create().withAudience(userId)
                // 2小时后token过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                // 以password作为token的密钥
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * @return com.staffing.entity.User
     * @author JngKang
     * @description 获取当前登录的用户信息
     */
    public static Employee getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(("token"));
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticEmployeeService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
