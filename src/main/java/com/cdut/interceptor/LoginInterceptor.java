package com.cdut.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
            return true;
        }

        String token = resolveToken(request);
        }

            }

            return true;
    }

    @Override
        UserContext.clear();
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        }
        return null;
    }

    }
}
