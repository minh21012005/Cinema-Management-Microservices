    package com.example.interceptor;

    import com.example.util.JwtUtil;
    import com.example.util.error.PermissionException;
    import com.example.util.error.UnauthorizedException;
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jws;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.stereotype.Component;
    import org.springframework.web.method.HandlerMethod;
    import org.springframework.web.servlet.HandlerInterceptor;

    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;

    @Component
    public class AuthorizationInterceptor implements HandlerInterceptor {
        private static final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

        @Autowired
        private RedisTemplate<String, String> redisTemplate;

        @Autowired
        private JwtUtil jwtUtil;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // Chỉ xử lý nếu handler là method của controller
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequiredPermission requiredPermission = handlerMethod.getMethodAnnotation(RequiredPermission.class);
            if (requiredPermission == null) {
                return true; // Không yêu cầu permission
            }

            String permission = requiredPermission.value();
            String token = request.getHeader("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                throw new UnauthorizedException("Missing or invalid Authorization header");
            }

            try {
                // Decode JWT
                String jwt = token.replace("Bearer ", "");
                Jws<Claims> claims = jwtUtil.validateAndParseToken(jwt);
                String userId = claims.getBody().getSubject(); // Lấy userId từ claim "sub"

                // Check permission trong Redis
                String redisKey = "user:permissions:" + userId;
                Object permValue = redisTemplate.opsForHash().get(redisKey, permission);
                if (permValue != null && "1".equals(permValue.toString())) {
                    logger.info("User {} has permission {}", userId, permission);
                    return true; // Có quyền
                } else {
                    throw new PermissionException("You do not have permission: " + permission);
                }
            } catch (PermissionException pe) {
                // Không có quyền
                throw pe;
            } catch (Exception e) {
                // Sai/expire JWT
                throw new UnauthorizedException("Invalid or expired token");
            }
        }
    }
