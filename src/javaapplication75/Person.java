package javaapplication75;

import Db.Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Person {

    public int id;
    public String firstname;
    public String lastname;
    public byte age;

    public Person(int id, String name, String lastName, byte age) {
        this.id = id;
        this.firstname = name;
        this.lastname = lastName;
        this.age = age;
    }

    public static List<Person> getAll() throws SQLException {
        Connection conn = Connector.getConnection();
        Statement st = conn.createStatement();
        ResultSet res = st.executeQuery("select * from person");
        List<Person> out = new ArrayList<>();
        while (res.next()) {
            out.add(new Person(
                    res.getInt(1),
                    res.getString(2),
                    res.getString(3),
                    res.getByte(4)
            ));
        }
        return out;
    }

    public static Person getById(int id) throws SQLException {
        Connection conn = Connector.getConnection();
        PreparedStatement st = conn.prepareStatement("select * from person where id=?");
        st.setInt(1, id);
        ResultSet res = st.executeQuery();
        res.next();
        return new Person(
                res.getInt(1),
                res.getString(2),
                res.getString(3),
                res.getByte(4)
        );

    }

    public void insert() throws SQLException {
        Connection conn = Connector.getConnection();
        PreparedStatement st = conn.prepareStatement("insert into person values("
                + "null, ?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setString(1, this.firstname);
        st.setString(2, this.lastname);
        st.setByte(3, this.age);
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        rs.next();
        this.id = rs.getInt(1);

    }

    public void update() throws SQLException {
        Connection conn = Connector.getConnection();
        PreparedStatement st = conn.prepareStatement("update person set firstname=?, lastname=?, age=? where id=?");
        st.setString(1, this.firstname);
        st.setString(2, this.lastname);
        st.setByte(3, this.age);
        st.setInt(4, this.id);
        st.executeUpdate();

    }

    public static void delete(int id) throws SQLException {
        Connection conn = Connector.getConnection();
        PreparedStatement st = conn.prepareStatement("delete from person where id=?");
        st.setInt(1, id);
        st.executeUpdate();
    }

    @Override
    public String toString() {
        return firstname + " " + lastname + " " + "age:" + age + " (id:" + id + ")";
    }

}
