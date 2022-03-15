package org.team4u.selector.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置工具类
 *
 * @author jay.wu
 */
public class ConfigUtil {

    /**
     * 配置是否为数组
     */
    public static boolean isArrayConfig(String jsonConfig) {
        return StrUtil.startWith(StrUtil.trim(jsonConfig), "[");
    }

    /**
     * 解析MapList结构配置
     */
    public static LinkedHashMap<String, Double> parseMapListConfig(String jsonConfig) {
        JSONArray config = JSONUtil.parseArray(jsonConfig, JSONConfig.create().setOrder(true));
        LinkedHashMap<String, Double> weightObjs = new LinkedHashMap<>();

        for (Object o : config) {
            JSONObject weightObj = (JSONObject) o;
            for (Map.Entry<String, Object> w : weightObj.entrySet()) {
                weightObjs.put(w.getKey(), Convert.toDouble(w.getValue()));
            }
        }

        return weightObjs;
    }
}