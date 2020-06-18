package org.team4u.core.util;

import cn.hutool.core.util.StrUtil;

public class EmojiUtil {

    private static boolean isNotEmoji(char character) {
        return (character == 0x0) || (character == 0x9) || (character == 0xA)
                || (character == 0xD)
                || ((character >= 0x20) && (character <= 0xD7FF))
                || ((character >= 0xE000) && (character <= 0xFFFD))
                || ((character >= 0x10000) && (character <= 0x10FFFF));
    }

    /**
     * 删除所有emoji或者其他非文字类型的字符
     */
    public static String removeAllEmojis(String source) {
        if (StrUtil.isBlank(source)) {
            return source;
        }

        int length = source.length();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char character = source.charAt(i);

            if (isNotEmoji(character)) {
                stringBuilder.append(character);
            }
        }

        return stringBuilder.toString();
    }
}