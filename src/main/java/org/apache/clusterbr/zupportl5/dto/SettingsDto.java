package org.apache.clusterbr.zupportl5.dto;

import java.sql.Timestamp;

import org.apache.clusterbr.zupportl5.utils.JsonUtil;

public class SettingsDto {

    private String idKey;
    private String value;
    private String label;
    private Long expiresAtLong;
    private Timestamp expiresAtTime;
    private Timestamp lastUpdate;

    public SettingsDto() {}

    public SettingsDto(String idKey, String value) {
        this.idKey = idKey;
        this.value = value;
    }

    public SettingsDto(String idKey, String value, String label, Long expiresAtLong, Timestamp expiresAtTime, Timestamp lastUpdate) {
        this.idKey = idKey;
        this.value = value;
        this.label = label;
        this.expiresAtLong = expiresAtLong;
        this.expiresAtTime = expiresAtTime;
        this.lastUpdate = lastUpdate;
    }

    //-- Getters & Setters
    
    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getExpiresAtLong() {
        return expiresAtLong;
    }

    public void setExpiresAtLong(Long expiresAtLong) {
        this.expiresAtLong = expiresAtLong;
    }

    public Timestamp getExpiresAtTime() {
        return expiresAtTime;
    }

    public void setExpiresAtTime(Timestamp expiresAtTime) {
        this.expiresAtTime = expiresAtTime;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
