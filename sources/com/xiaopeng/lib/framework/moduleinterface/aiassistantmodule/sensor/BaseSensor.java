package com.xiaopeng.lib.framework.moduleinterface.aiassistantmodule.sensor;

import java.lang.reflect.Field;
/* loaded from: classes.dex */
public abstract class BaseSensor {
    private ISensorCallback mSensorCallback;
    private ISensorListener mSensorListener;

    public void doExtra(String extra) {
    }

    public abstract void initField();

    public void refreshField(String field) {
    }

    public abstract String sensorName();

    public abstract void startSensing();

    public abstract void stopSensing();

    protected BaseSensor() {
        initField();
    }

    public void setSensorListener(ISensorListener sensorListener) {
        this.mSensorListener = sensorListener;
    }

    public void setSensorCallback(ISensorCallback sensorCallback) {
        this.mSensorCallback = sensorCallback;
    }

    public ISensorCallback getSensorCallback() {
        return this.mSensorCallback;
    }

    public ISensorListener getSensorListener() {
        return this.mSensorListener;
    }

    protected void sensing(String field, String value) {
        if (getSensorListener() != null) {
            getSensorListener().onSensorChange(sensorName(), field, value);
        }
    }

    protected void sensing(String field, Object value) {
        sensing(field, String.valueOf(value));
    }

    public String onRequestSensorValue(String fieldName) {
        String str;
        try {
            Field fieldByClasss = getFieldByClasss(fieldName, this);
            refreshField(fieldName);
            fieldByClasss.setAccessible(true);
            str = String.valueOf(fieldByClasss.get(this));
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
        }
        if (getSensorCallback() != null) {
            getSensorCallback().onSensorReponse(sensorName(), fieldName, str);
        }
        return str;
    }

    private Field getFieldByClasss(String fieldName, Object object) {
        Field field = null;
        for (Class<?> cls = object.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            try {
                field = cls.getDeclaredField(fieldName);
            } catch (Exception unused) {
            }
        }
        return field;
    }
}
