package org.team4u.base.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 身份证值对象
 *
 * @author jay.wu
 */
public class IdCard {

    /**
     * 56个民族
     */
    public static List<String> NATIONS = Arrays.asList(
            "阿昌", "鄂温克", "傈僳", "水",
            "白", "高山", "珞巴", "塔吉克",
            "保安", "仡佬", "满", "塔塔尔",
            "布朗", "哈尼", "毛南", "土家",
            "布依", "哈萨克", "门巴", "土",
            "朝鲜", "汉", "蒙古", "佤",
            "达斡尔", "赫哲", "苗", "维吾尔",
            "傣", "回", "仫佬", "乌孜别克",
            "德昂", "基诺", "纳西", "锡伯",
            "东乡", "京", "怒", "瑶",
            "侗", "景颇", "普米", "彝",
            "独龙", "柯尔克孜", "羌", "裕固",
            "俄罗斯", "拉祜", "撒拉", "藏",
            "鄂伦春", "黎", "畲", "壮", "穿青人"
    );
    private Front front;
    private Back back;

    public Front getFront() {
        return front;
    }

    public IdCard setFront(Front front) {
        this.front = front;
        return this;
    }

    public Back getBack() {
        return back;
    }

    public IdCard setBack(Back back) {
        this.back = back;
        return this;
    }

    public static class IdCardNumber {
        private final String provinceCode;
        private final String cityCode;
        private final DateTime birthDate;
        private final Integer gender;
        private final int age;

        public IdCardNumber(String idCardNumber) {
            IdcardUtil.Idcard idcard = new IdcardUtil.Idcard(idCardNumber);
            this.provinceCode = idcard.getProvinceCode();
            this.cityCode = idcard.getCityCode();
            this.birthDate = idcard.getBirthDate();
            this.gender = idcard.getGender();
            this.age = idcard.getAge();
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public String getCityCode() {
            return cityCode;
        }

        public DateTime getBirthDate() {
            return birthDate;
        }

        public Integer getGender() {
            return gender;
        }

        public int getAge() {
            return age;
        }
    }

    public static class Front {
        public final static Pattern NAME_PATTERN = Pattern.compile(RegexPool.CHINESES + "[·•]*" + RegexPool.CHINESE + "*");

        private String name;
        private String address;
        private String nation;
        private IdCardNumber idCardNumber;

        public Front(String name, String address, String nation, String idCardNumber) {
            setName(name);
            setAddress(address);
            setNation(nation);
            setIdCardNumber(idCardNumber);
        }

        public String getName() {
            return name;
        }

        private void setName(String name) {
            if (StrUtil.isBlank(name)) {
                return;
            }

            name = name.replace(" ", "");

            if (name.length() > 20) {
                return;
            }

            if (!Validator.isMatchRegex(NAME_PATTERN, name)) {
                return;
            }

            this.name = name;
        }

        public IdCardNumber getIdCardNumber() {
            return idCardNumber;
        }

        private void setIdCardNumber(String idCardNumber) {
            if (StrUtil.isBlank(idCardNumber)) {
                return;
            }

            this.idCardNumber = new IdCardNumber(idCardNumber);
        }

        public String getAddress() {
            return address;
        }

        private void setAddress(String address) {
            this.address = address;
        }

        public String getNation() {
            return nation;
        }

        private void setNation(String nation) {
            if (!NATIONS.contains(nation)) {
                return;
            }

            this.nation = nation;
        }
    }

    public static class Back {
        /**
         * 签发机关
         */
        private String issuedBy;
        /**
         * 有效期类型
         */
        private ValidityPeriodType validityPeriodType;
        /**
         * 有效期开始时间
         */
        private DateTime validityStartTime;
        /**
         * 有效期结束时间
         */
        private DateTime validityEndTime;

        public Back(String issuedBy, String validityPeriod) {
            setIssuedBy(issuedBy);
            parseValidityPeriod(validityPeriod);
        }

        private void parseValidityPeriod(String validityPeriod) {
            if (StrUtil.isBlank(validityPeriod)) {
                return;
            }

            if (StrUtil.equals(validityPeriod, "长期")) {
                setValidityPeriodType(ValidityPeriodType.LONG);
                return;
            }

            List<String> validityPeriods = StrUtil.splitTrim(validityPeriod, "-");
            setValidityStartTime(CollUtil.getFirst(validityPeriods));
            setValidityEndTime(CollUtil.get(validityPeriods, 1));
            setValidityPeriodType(ValidityPeriodType.SHORT);
        }

        private DateTime parseDate(String date) {
            if (StrUtil.isBlank(date)) {
                return null;
            }

            try {
                return DateUtil.parseDate(date);
            } catch (Exception ignore) {
                return null;
            }
        }

        public ValidityPeriodType getValidityPeriodType() {
            return validityPeriodType;
        }

        private void setValidityPeriodType(ValidityPeriodType validityPeriodType) {
            if (validityPeriodType == ValidityPeriodType.SHORT) {
                if (getValidityStartTime() == null && getValidityEndTime() == null) {
                    return;
                }
            }

            this.validityPeriodType = validityPeriodType;
        }

        public String getIssuedBy() {
            return issuedBy;
        }

        private void setIssuedBy(String issuedBy) {
            this.issuedBy = issuedBy;
        }

        public DateTime getValidityStartTime() {
            return validityStartTime;
        }

        private void setValidityStartTime(String validityStartTime) {
            this.validityStartTime = parseDate(validityStartTime);
        }

        public DateTime getValidityEndTime() {
            return validityEndTime;
        }

        private void setValidityEndTime(String validityEndTime) {
            this.validityEndTime = parseDate(validityEndTime);
        }

        public enum ValidityPeriodType {

            LONG, SHORT
        }
    }
}