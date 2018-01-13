package org.team4u.kit.core.codec.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.team4u.kit.core.action.Each;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.codec.Codec;
import org.team4u.kit.core.util.ValueUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class UrlToMapCodec implements Codec<String, Map<String, String>> {

    public static String serialize(Map<String, ?> params, final Function<String, String> encoder) {
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

    @Override
    public Map<String, String> encode(String urlParams) {
        Map<String, String> result = new HashMap<String, String>();

        if (urlParams == null) {
            return result;
        }

        List<String> pairStrings = StrUtil.split(urlParams, '&', true, true);
        for (String pairString : pairStrings) {
            String[] pair = pairString.split("=");

            if (pair.length == 2) {
                String key = URLUtil.decode(pair[0], CharsetUtil.UTF_8);
                String value = URLUtil.decode(pair[1], CharsetUtil.UTF_8);

                String oldValue = result.get(key);
                if (oldValue != null) {
                    value = oldValue + "," + value;
                }

                result.put(key, value);
            }
        }

        return result;
    }

    @Override
    public String decode(Map<String, String> params) {
        return serialize(params, new Function<String, String>() {
            @Override
            public String invoke(String obj) {
                return URLUtil.encode(obj, CharsetUtil.UTF_8);
            }
        });
    }
}
