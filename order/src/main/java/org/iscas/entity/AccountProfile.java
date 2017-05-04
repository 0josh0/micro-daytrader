package org.iscas.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
/**
 * Created by andyren on 2016/6/28.
 */
@Entity(name = "accountprofile")
@Table(name="accountprofile")
public class AccountProfile implements java.io.Serializable {

    @Id
    @Column(name = "userid", nullable = false)
    @NotNull
    private String userID;

    @Column(name = "passwd")
    private String passwd;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "creditcard")
    private String creditCard;

    @Column(name = "email")
    private String email;
    
    @Column(name = "address")
    private String address;

    public AccountProfile() {
    }

    public AccountProfile(String userID,
                          String password,
                          String fullName,
                          String creditCard,
                          String email,
                          String address) {
        setUserID(userID);
        setPassword(password);
        setFullName(fullName);
        setCreditCard(creditCard);
        setEmail(email);
        setAddress(address);
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.userID != null ? this.userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountProfile)) {
            return false;
        }
        AccountProfile other = (AccountProfile)object;
        if (this.userID != other.userID && (this.userID == null || !this.userID.equals(other.userID))) return false;
        return true;
    }
}
