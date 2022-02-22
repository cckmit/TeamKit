package org.team4u.command.infrastructure.util;

import cn.hutool.core.util.XmlUtil;

/**
 * Xml提取器
 *
 * @author jay.wu
 */
public class XmlExtractor extends JsonExtractor {

    protected Object extractableSource(Object source) {
        if (source instanceof String) {
            return XmlUtil.xmlToMap((String) source);
        }

        return source;
    }
}