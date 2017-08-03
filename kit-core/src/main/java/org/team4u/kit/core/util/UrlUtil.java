package org.team4u.kit.core.util;


import com.xiaoleilu.hutool.util.StrUtil;
import org.team4u.kit.core.action.Each;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.error.ExceptionUtil;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlUtil {

    private static final String UTF8 = "utf-8";

    public static String encode(String url) {
        if (url == null) {
            return null;
        }

        try {
            return URLEncoder.encode(url, UTF8);
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public static String decode(String url) {
        if (url == null) {
            return null;
        }

        try {
            return URLDecoder.decode(url, UTF8);
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }


    public static String serialize(Map<String, ?> params) {
        return serialize(params, new Function<String, String>() {
            @Override
            public String invoke(String obj) {
                return obj;
            }
        });
    }

    public static String serializeWithEncode(Map<String, ?> params) {
        return serialize(params, new Function<String, String>() {
            @Override
            public String invoke(String obj) {
                return encode(obj);
            }
        });
    }

    public static Map<String, String> deserialize(String urlParams) {
        Map<String, String> result = new HashMap<String, String>();

        if (urlParams == null) {
            return result;
        }

        List<String> pairStrings = StrUtil.split(urlParams, '&', true, true);
        for (String pairString : pairStrings) {
            String[] pair = pairString.split("=");

            if (pair.length == 2) {
                String key = decode(pair[0]);
                String value = decode(pair[1]);

                String oldValue = result.get(key);
                if (oldValue != null) {
                    value = oldValue + "," + value;
                }

                result.put(key, value);
            }
        }

        return result;
    }

    public static Map<String, List<String>> deserializeWithList(String urlParams) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        if (urlParams == null) {
            return result;
        }

        List<String> pairStrings = StrUtil.split(urlParams, '&', true, true);
        for (String pairString : pairStrings) {
            String[] pair = pairString.split("=");

            if (pair.length == 2) {
                String key = decode(pair[0]);
                String value = decode(pair[1]);

                List<String> values = result.get(key);

                if (values == null) {
                    values = new ArrayList<String>();
                    result.put(key, values);
                }

                values.add(value);
            }
        }

        return result;
    }

    private static String serialize(Map<String, ?> params, final Function<String, String> encoder) {
        final StringBuilder sb = new StringBuilder();

        for (final Map.Entry<String, ?> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            ValueUtil.each(entry.getValue(), new Each<Object>() {
                @Override
                public void invoke(int index, Object value, int length)
                        throws ExitLoop, ContinueLoop, LoopException {
                    if (sb.length() > 0) {
                        sb.append('&');
                    }

                    sb.append(encoder.invoke(entry.getKey()));
                    sb.append('=');
                    sb.append(encoder.invoke(value.toString()));
                }
            });
        }

        return sb.toString();
    }
}