package org.example;


import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        var conn = DriverManager.getConnection("jdbc:h2:mem:san;NON_KEYWORDS=user");
        var dao = new UserDAO(conn);

        var userMaria = new User("Maria", "88888");
        dao.save(userMaria);

        var userJohn = new User("John", "999999");
        dao.save(userJohn);

        var dbUserMaria = dao.find(userMaria.getId()).get();
        var dbUserJohn = dao.find(userJohn.getId()).get();


        System.out.println("User Maria saved correctly? => " + userMaria.equals(dbUserMaria));
        System.out.println("User John saved correctly? => " + userJohn.equals(dbUserJohn));

        dao.delete(userJohn.getId());
        if (dao.find(userJohn.getId()).isEmpty()) {
            System.out.println("User John deleted from DB? => " + dao.find(userJohn.getId()).isEmpty());
            userJohn = null;
        }
    }
}