package org.apache.clusterbr.zupportl5.interceptor;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/LocaleInterceptor_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/LocaleInterceptor_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/LocaleInterceptor_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String language = request.getHeader("Accept-Language");
        Locale locale = (language != null && !language.isEmpty()) ? Locale.forLanguageTag(language) : Locale.ENGLISH;

        LocaleContextHolder.setLocale(locale);

        return true;
    }
}

