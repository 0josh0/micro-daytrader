package org.iscas.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.iscas.util.TradeConfig;
import org.iscas.util.Log;

/**
 * Created by andyren on 2016/6/28.
 */
@Entity(name = "accountprofile")
@Table(name = "accountprofile")
@NamedQueries( {
        @NamedQuery(name = "accountprofile.findByAddress", query = "SELECT a FROM accountprofile a WHERE a.address = :address"),
        @NamedQuery(name = "accountprofile.findByPasswd", query = "SELECT a FROM accountprofile a WHERE a.passwd = :passwd"),
        @NamedQuery(name = "accountprofile.findByUserid", query = "SELECT a FROM accountprofile a WHERE a.userID = :userid"),
        @NamedQuery(name = "accountprofile.findByEmail", query = "SELECT a FROM accountprofile a WHERE a.email = :email"),
        @NamedQuery(name = "accountprofile.findByCreditcard", query = "SELECT a FROM accountprofile a WHERE a.creditCard = :creditcard"),
        @NamedQuery(name = "accountprofile.findByFullname", query = "SELECT a FROM accountprofile a WHERE a.fullName = :fullname")
    })
public class AccountProfile implements java.io.Serializable {

    /* Accessor methods for persistent fields */

    @Id
    @Column(name = "USERID", nullable = false)
    @NotNull
    private String userID;              /* userID */
    
    @Column(name = "PASSWD")
    private String passwd;              /* password */
    
    @Column(name = "FULLNAME")
    private String fullName;            /* fullName */
    
    @Column(name = "ADDRESS")
    private String address;             /* address */
    
    @Column(name = "EMAIL")
    private String email;               /* email */
    
    @Column(name = "CREDITCARD")
    private String creditCard;          /* creditCard */
    
    @OneToOne(mappedBy="profile", fetch=FetchType.LAZY)
    private Account account;

//    @Version
//    private Integer optLock;

    public AccountProfile() {
    }

    public AccountProfile(String userID,
                          String password,
                          String fullName,
                          String address,
                          String email,
                          String creditCard) {
        setUserID(userID);
        setPassword(password);
        setFullName(fullName);
        setAddress(address);
        setEmail(email);
        setCreditCard(creditCard);
    }

    public static AccountProfile getRandomInstance() {
        return new AccountProfile(
                TradeConfig.rndUserID(),                        // userID
                TradeConfig.rndUserID(),                        // passwd
                TradeConfig.rndFullName(),                      // fullname
                TradeConfig.rndAddress(),                       // address
                TradeConfig.rndEmail(TradeConfig.rndUserID()),  //email
                TradeConfig.rndCreditCard()                     // creditCard
        );
    }

    public String toString() {
        return "\n\tAccount Profile Data for userID:" + getUserID()
                + "\n\t\t   passwd:" + getPassword()
                + "\n\t\t   fullName:" + getFullName()
                + "\n\t\t    address:" + getAddress()
                + "\n\t\t      email:" + getEmail()
                + "\n\t\t creditCard:" + getCreditCard()
                ;
    }

    public String toHTML() {
        return "<BR>Account Profile Data for userID: <B>" + getUserID() + "</B>"
                + "<LI>   passwd:" + getPassword() + "</LI>"
                + "<LI>   fullName:" + getFullName() + "</LI>"
                + "<LI>    address:" + getAddress() + "</LI>"
                + "<LI>      email:" + getEmail() + "</LI>"
                + "<LI> creditCard:" + getCreditCard() + "</LI>"
                ;
    }

    public void print() {
        Log.log(this.toString());
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return passwd;
    }

    public void setPassword(String password) {
        this.passwd = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.userID != null ? this.userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountProfile)) {
            return false;
        }
        AccountProfile other = (AccountProfile)object;
        if (this.userID != other.userID && (this.userID == null || !this.userID.equals(other.userID))) return false;
        return true;
    }
}
