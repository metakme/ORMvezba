package javaapplication75; 

public class JavaApplication75 { 

    public static void main(String[] args) { 
        Test t = new Test("Jack","The Ripper",(byte)43);
        t.insert();
        t.setLastname("Slash");
        t.setAge((byte)53);
        t.insert();
        System.out.println(t.getById(t.getWrongIdName()));
        System.out.println("\n----------------------");
        t.setFirst__name("V");
        t.setLastname("for Vendetta");
        t.setAge((byte)37);
        t.update();
        System.out.println(t.getById(t.getWrongIdName()));
        System.out.println("\n----------------------");
        t.getAll().stream().forEach(s-> System.out.println(s.toString()));
    } 
} 