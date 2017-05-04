package org.iscas.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by andyren on 2016/6/28.
 */

@Entity(name = "account")
@Table(name = "account")
public class Account implements Serializable {

    @TableGenerator(
            name="accountIdGen",
            table="keygen",
            pkColumnName="keyname",
            valueColumnName="keyval",
            pkColumnValue="account",
            allocationSize=1000)

    @Id
    @GeneratedValue(strategy=GenerationType.TABLE, generator="accountIdGen")
    @Column(name = "accountid", nullable = false)
    @NotNull
    private Integer accountID;

    @Column(name = "profile_userid")
    private String profileUserID;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "openbalance")
    private BigDecimal openBalance;

    @Column(name = "creationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "lastlogin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @NotNull
    @Column(name = "logincount", nullable = false)
    private Integer loginCount;

    @NotNull
    @Column(name = "logoutcount", nullable = false)
    private Integer logoutCount;

    public Account() {
    }

    public Account(Integer accountID,
                   String profileUserID,
                   BigDecimal balance,
                   BigDecimal openBalance,
                   Date creationDate,
                   Date lastLogin,
                   Integer loginCount,
                   Integer logoutCount
    ) {
        setAccountID(accountID);
        setProfileUserID(profileUserID);
        setBalance(balance);
        setOpenBalance(openBalance);
        setCreationDate(creationDate);
        setLastLogin(lastLogin);
        setLoginCount(loginCount);
        setLogoutCount(logoutCount);
    }

    public Account(Integer loginCount,
                   Integer logoutCount,
                   Date lastLogin,
                   Date creationDate,
                   BigDecimal balance,
                   BigDecimal openBalance,
                   String profileUserID) {
        setLoginCount(loginCount);
        setLogoutCount(logoutCount);
        setLastLogin(lastLogin);
        setCreationDate(creationDate);
        setBalance(balance);
        setOpenBalance(openBalance);
        setProfileUserID(profileUserID);
    }

    public Account(Integer accountID,
                   int loginCount,
                   int logoutCount,
                   Date lastLogin,
                   Date creationDate,
                   BigDecimal balance,
                   BigDecimal openBalance,
                   String profileUserID) {
        setAccountID(accountID);
        setLoginCount(loginCount);
        setLogoutCount(logoutCount);
        setLastLogin(lastLogin);
        setCreationDate(creationDate);
        setBalance(balance);
        setOpenBalance(openBalance);
        setProfileUserID(profileUserID);
    }

    public String toString() {
        return "\n\tAccount Data for account: " + getAccountID()
                + "\n\t\t   loginCount:" + getLoginCount()
                + "\n\t\t  logoutCount:" + getLogoutCount()
                + "\n\t\t    lastLogin:" + getLastLogin()
                + "\n\t\t creationDate:" + getCreationDate()
                + "\n\t\t      balance:" + getBalance()
                + "\n\t\t  openBalance:" + getOpenBalance()
                + "\n\t\t    profileID:" + getProfileUserID()
                ;
    }

    public String toHTML() {
        return "<BR>Account Data for account: <B>" + getAccountID() + "</B>"
                + "<LI>   loginCount:" + getLoginCount() + "</LI>"
                + "<LI>  logoutCount:" + getLogoutCount() + "</LI>"
                + "<LI>    lastLogin:" + getLastLogin() + "</LI>"
                + "<LI> creationDate:" + getCreationDate() + "</LI>"
                + "<LI>      balance:" + getBalance() + "</LI>"
                + "<LI>  openBalance:" + getOpenBalance() + "</LI>"
                + "<LI>    profileID:" + getProfileUserID() + "</LI>"
                ;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getLogoutCount() {
        return logoutCount;
    }

    public void setLogoutCount(Integer logoutCount) {
        this.logoutCount = logoutCount;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    public String getProfileUserID() {
        return profileUserID;
    }

    public void setProfileUserID(String profileUserID) {
        this.profileUserID = profileUserID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.accountID != null ? this.accountID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof org.iscas.entity.Account)) {
            return false;
        }
        org.iscas.entity.Account other = (org.iscas.entity.Account)object;
        if (this.accountID != other.accountID && (this.accountID == null || !this.accountID.equals(other.accountID))) return false;
        return true;
    }
}

