package org.team4u.kit.core.codec.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.codec.Codec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class UrlToMapListCodec implements Codec<String, Map<String, List<String>>> {

    @Override
    public Map<String, List<String>> encode(String urlParams) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        if (urlParams == null) {
            return result;
        }

        List<String> pairStrings = StrUtil.split(urlParams, '&', true, true);
        for (String pairString : pairStrings) {
            String[] pair = pairString.split("=");

            if (pair.length == 2) {
                String key = URLUtil.decode(pair[0], CharsetUtil.UTF_8);
                String value = URLUtil.decode(pair[1], CharsetUtil.UTF_8);

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

    @Override
    public String decode(Map<String, List<String>> params) {
        return UrlToMapCodec.serialize(params, new Function<String, String>() {
            @Override
            public String invoke(String obj) {
                return URLUtil.encode(obj, CharsetUtil.UTF_8);
            }
        });
    }
}
