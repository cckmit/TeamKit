package org.team4u.ddd.notification;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class NotificationReader {

    private JSONObject representation;
    private JSONObject event;

    public NotificationReader(String notification) {
        this(JSON.parseObject(notification));
    }

    public NotificationReader(JSONObject representationObject) {
        setRepresentation(representationObject);
        this.setEvent(this.representation().getJSONObject("event"));
    }

    public long notificationId() {
        return representation().getLongValue("notificationId");
    }

    public String notificationIdAsString() {
        return representation().getString("notificationId");
    }

    public Date occurredOn() {
        return representation().getDate("occurredOn");
    }

    public String typeName() {
        return representation().getString("typeName");
    }

    public int version() {
        return representation().getIntValue("version");
    }

    @SuppressWarnings("unchecked")
    public <T> T eventValue(String expression) {
        return (T) BeanUtil.getProperty(event(), expression);
    }

    public <T> T eventValue(String expression, T defaultValue) {
        return ObjectUtil.defaultIfNull(eventValue(expression), defaultValue);
    }

    private JSONObject event() {
        return this.event;
    }

    private void setEvent(JSONObject event) {
        this.event = event;
    }

    private void setRepresentation(JSONObject representation) {
        this.representation = representation;
    }

    public JSONObject representation() {
        return representation;
    }
}