package com.hsbc.web.config;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

//@Component
public class ResponseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        ResponseWrapper wrapperResponse = new ResponseWrapper((HttpServletResponse) response);//转换成代理类
        filterChain.doFilter(request, wrapperResponse);
        byte[] content = wrapperResponse.getContent();//获取返回值
        if (content.length > 0) {
            String str = new String(content, StandardCharsets.UTF_8);
            ServletOutputStream outputStream = response.getOutputStream();
            if (str.length() > 0) {
                outputStream.write(str.getBytes());
                outputStream.flush();
                outputStream.close();
                response.flushBuffer();
            }
        }
    }
}
