package org.team4u.base.lang.lazy;

import cn.hutool.core.convert.Convert;
import cn.hutool.log.Log;
import org.team4u.base.masker.Maskers;

/**
 * 默认的懒加载值日志格式化器
 * <p>
 * debug模式下将直接展示原始值，其他模式掩码展示
 *
 * @author jay.wu
 */
public class DefaultLazyValueFormatter implements LazyValueFormatter<Object> {

    @Override
    public String format(Log log, Object value) {
        if (value == null) {
            return null;
        }

        String valueString = Convert.toStr(value);
        if (log.isDebugEnabled()) {
            return valueString;
        }

        return Maskers.Type.PERCENT66_LIMIT10.mask(valueString);
    }
}