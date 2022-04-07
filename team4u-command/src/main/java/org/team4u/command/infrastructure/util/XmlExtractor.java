package org.team4u.command.infrastructure.util;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.log.Log;
import org.team4u.base.lang.lazy.LazySupplier;
import org.team4u.base.log.LogMessage;
import org.team4u.base.serializer.json.JsonSerializers;

/**
 * Xml提取器
 *
 * @author jay.wu
 */
public class XmlExtractor extends JsonExtractor {

    private final Log log = Log.get();

    private static final LazySupplier<XmlExtractor> instance = LazySupplier.of(XmlExtractor::new);

    public static XmlExtractor getInstance() {
        return instance.get();
    }

    protected Object extractableSource(Object source) {
        if (source instanceof String) {
            source = XmlUtil.xmlToMap((String) source);
        }

        if (log.isDebugEnabled()) {
            log.debug(LogMessage.create(this.getClass().getSimpleName(), "extractableSource")
                    .success()
                    .append("source", JsonSerializers.getInstance().serialize(source))
                    .toString());
        }
        return source;
    }
}