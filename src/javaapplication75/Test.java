package javaapplication75;

import Db.ActiveRecord;
import Db.AREntity;
import Db.Column;
import Db.Id;

@AREntity(tableName = "person")
public class Test extends ActiveRecord {
    
    @Id
    @Column(column = "id")
    private int wrongIdName;
    
    String asdasd;
    
    @Column (column = "firstname")
    private String first__name;
    
    @Column
    private String lastname;
    
    Object o;
    
    @Column
    private byte age;
    
    int bla;
    
    public Test(){
        
    }

    public Test(String firstname, String lastname, byte age) {
        this.first__name = firstname;
        this.lastname = lastname;
        this.age = age;
    }
    
    

    @Override
    public String toString() {
        return wrongIdName +" "+ first__name +" "+ lastname +" "+ age;
    }

    public int getWrongIdName() {
        return wrongIdName;
    }

    public void setWrongIdName(int wrongIdName) {
        this.wrongIdName = wrongIdName;
    }

    public String getFirst__name() {
        return first__name;
    }

    public void setFirst__name(String first__name) {
        this.first__name = first__name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

}
