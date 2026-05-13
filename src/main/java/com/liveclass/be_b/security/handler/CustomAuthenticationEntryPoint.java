package com.liveclass.be_b.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.common.response.SecurityResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        SecurityResponseWriter.write(response, objectMapper, ErrorCode.AUTHENTICATION_REQUIRED);
    }
}
