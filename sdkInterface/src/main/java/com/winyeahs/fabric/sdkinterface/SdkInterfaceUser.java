package com.winyeahs.fabric.sdkinterface;

import com.winyeahs.fabric.sdkinterface.base.SdkInterfaceBase;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkInterfaceUser extends SdkInterfaceBase implements User, Serializable {

    // 名称
    private String name;
    // 规则
    private Set<String> roles;
    // 账户
    private String account;
    // 从属联盟
    private String affiliation;
    // 组织id
    private String mspId;
    // 登记操作
    private Enrollment enrollment = null;

    SdkInterfaceUser(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return this.name;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    @Override
    public String getAccount() {
        return this.account;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }
    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }
    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }
    @Override
    public String getMspId() {
        return this.mspId;
    }
}
