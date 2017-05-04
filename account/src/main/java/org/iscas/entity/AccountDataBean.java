package org.iscas.entity;

import java.io.Serializable;

/**
 * Created by Summer on 2016/11/15.
 */
public class AccountDataBean implements Serializable{
    private String fullname;
    private String address;
    private String email;
    private String userID;
    private String passwd;
    private String cpasswd;
    private String creditcard;
    private String openBalance;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCpasswd() {
        return cpasswd;
    }

    public void setCpasswd(String cpasswd) {
        this.cpasswd = cpasswd;
    }

    public String getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

    public String getOpenBalance() {
        return openBalance;
    }

    public void setOpenBalance(String openBalance) {
        this.openBalance = openBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDataBean)) return false;

        AccountDataBean that = (AccountDataBean) o;

        if (!getFullname().equals(that.getFullname())) return false;
        if (!getAddress().equals(that.getAddress())) return false;
        if (!getEmail().equals(that.getEmail())) return false;
        if (!getUserID().equals(that.getUserID())) return false;
        if (!getPasswd().equals(that.getPasswd())) return false;
        if (!getCpasswd().equals(that.getCpasswd())) return false;
        if (!getCreditcard().equals(that.getCreditcard())) return false;
        return getOpenBalance().equals(that.getOpenBalance());

    }

    @Override
    public int hashCode() {
        int result = getFullname().hashCode();
        result = 31 * result + getAddress().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getUserID().hashCode();
        result = 31 * result + getPasswd().hashCode();
        result = 31 * result + getCpasswd().hashCode();
        result = 31 * result + getCreditcard().hashCode();
        result = 31 * result + getOpenBalance().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AccountDataBean{" +
                "fullname='" + fullname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", userID='" + userID + '\'' +
                ", passwd='" + passwd + '\'' +
                ", cpasswd='" + cpasswd + '\'' +
                ", creditcard='" + creditcard + '\'' +
                ", openBalance='" + openBalance + '\'' +
                '}';
    }

    public AccountDataBean(String fullname, String address, String email, String userID, String passwd, String cpasswd, String creditcard, String openBalance) {
        this.fullname = fullname;
        this.address = address;
        this.email = email;
        this.userID = userID;
        this.passwd = passwd;
        this.cpasswd = cpasswd;
        this.creditcard = creditcard;
        this.openBalance = openBalance;
    }
    public AccountDataBean() {
    }
}
