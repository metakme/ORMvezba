package javaapplication75;

import Db.AREntity;
import Db.Column;
import Db.Id;

@AREntity(tableName = "person")
public class AnotherPerson {
    @Id
    @Column
    private int id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column
    private byte age;

    public AnotherPerson() {
    }

    public AnotherPerson(String firstname, String lastname, byte age) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    
}
