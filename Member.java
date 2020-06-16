import java.io.Serializable;

/*
    File:               Member.java
    Date:               20/03/19
    Author:             Ethan Cannon
    Student Number:     12051468
    Purpose: This file is the Member object, used to define the structure of the members information
*/

public class Member implements Serializable {

    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    public Member(String firstName, String lastName, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    Member() {
        this("", "", "", "");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
