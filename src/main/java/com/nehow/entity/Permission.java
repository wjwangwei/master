package com.nehow.entity;

import java.util.Date;

public class Permission {
    private Long id;

    private String permission;

    private String permissionType;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private String updater;

    public Permission(Long id, String permission, String permissionType, String remark, Date createTime, Date updateTime, String updater) {
        this.id = id;
        this.permission = permission;
        this.permissionType = permissionType;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.updater = updater;
    }

    public Permission() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType == null ? null : permissionType.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater == null ? null : updater.trim();
    }
}