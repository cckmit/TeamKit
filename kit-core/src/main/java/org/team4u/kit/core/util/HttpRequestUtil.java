package org.team4u.kit.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.log.LogMessage;

import java.util.Map;

/**
 * HTTP请求帮助类
 *
 * @author Jay Wu
 */
public class HttpRequestUtil {

    private static Log log = LogFactory.get();

    /**
     * 请求
     *
     * @param request           HttpRequest对象
     * @param params            请求参数对象
     * @param responseReference 响应引用类
     * @param logMessage        日志对象
     * @param <T>               响应类型
     * @return 响应对象
     */
    public static <T extends ResultStatus> T request(HttpRequest request,
                                                     Object params,
                                                     final TypeReference<T> responseReference,
                                                     LogMessage logMessage) {
        return request(request, params, new Function<String, T>() {
            @Override
            public T invoke(String body) {
                return JSON.parseObject(body, responseReference);
            }
        }, logMessage);
    }

    /**
     * 请求
     *
     * @param request       HttpRequest对象
     * @param params        请求参数对象
     * @param responseClass 响应类
     * @param logMessage    日志对象
     * @param <T>           响应类型
     * @return 响应对象
     */
    public static <T extends ResultStatus> T request(HttpRequest request,
                                                     Object params,
                                                     final Class<T> responseClass,
                                                     LogMessage logMessage) {
        return request(request, params, new Function<String, T>() {
            @Override
            public T invoke(String body) {
                return JSON.parseObject(body, responseClass);
            }
        }, logMessage);
    }

    /**
     * 请求
     *
     * @param request           HttpRequest对象
     * @param params            请求参数对象
     * @param responseConverter 响应转换类
     * @param logMessage        日志对象
     * @param <T>               响应类型
     * @return 响应对象
     */
    public static <T extends ResultStatus> T request(HttpRequest request,
                                                     Object params,
                                                     Function<String, T> responseConverter,
                                                     LogMessage logMessage) {
        Map<String, Object> mapParams = BeanUtil.beanToMap(params);
        LogMessage lm = logMessage.append("params", mapParams);

        try {
            log.debug(lm.processing().toString());

            HttpResponse response = request.form(mapParams).execute();

            if (response == null) {
                log.error(lm.fail()
                        .append("response", null)
                        .toString());
                return null;
            }

            lm.append("httpStatus", response.getStatus()).append("body", response.body());

            if (response.getStatus() != HttpStatus.HTTP_OK) {
                log.error(lm.fail().toString());
                return null;
            }

            String body = response.body();
            if (body == null) {
                log.error(lm.fail().toString());
                return null;
            }

            T result = responseConverter.invoke(body);

            if (result.hasError()) {
                log.error(lm.fail().toString());
                return null;
            }

            log.debug(lm.success().toString());

            return result;
        } catch (Exception e) {
            log.error(e, lm.fail().toString());
            return null;
        }
    }

    public interface ResultStatus {

        /**
         * 是否包含业务错误
         */
        boolean hasError();
    }
}