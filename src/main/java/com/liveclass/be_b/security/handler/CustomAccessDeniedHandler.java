package com.liveclass.be_b.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liveclass.be_b.common.exception.ErrorCode;
import com.liveclass.be_b.common.response.SecurityResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        SecurityResponseWriter.write(response, objectMapper, ErrorCode.ACCESS_DENIED);
    }
}
