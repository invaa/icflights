package com.ryanair.icflights.controller;

import com.ryanair.icflights.dto.ErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Custom error handler controller.
 */
@RestController
public final class CustomErrorController implements ErrorController {
    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ErrorDto error(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return new ErrorDto(
                response.getStatus(),
                errorAttributes.getErrorAttributes(
                        new ServletRequestAttributes(request),
                        false)
                );
    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
