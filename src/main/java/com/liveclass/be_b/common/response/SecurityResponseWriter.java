package com.liveclass.be_b.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liveclass.be_b.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class SecurityResponseWriter {

    private SecurityResponseWriter() {}

    public static void write(HttpServletResponse response, ObjectMapper objectMapper, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ApiResponse.of(errorCode));
    }
}
