package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBC extends ConnectionHandler implements GenericQueriesDao<User> {
    private static UserDaoJDBC instance = null;

    private UserDaoJDBC() {
    }

    public static UserDaoJDBC getInstance() {
        if (instance == null) {
            instance = new UserDaoJDBC();
        }
        return instance;
    }


    @Override
    public void add(User user) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("INSERT INTO users (user_name, password) VALUES  (?, ?) RETURNING id; ");
        statement.setString(1, user.getUsername());
        statement.setString(2, Utils.hashPass(user.getPassword()));
        ResultSet result = statement.executeQuery();
        int cartId;
        while (result.next()) {
            cartId = result.getInt("id");
            user.setId(cartId);
        }
        statement.close();
    }

    public User find(String username) throws SQLException {
        User user;
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM users WHERE user_name = ?;");
        statement.setString(1, username);

        ResultSet results = statement.executeQuery();

        int userId = 0;
        String userName = null;
        String password = null;

        while (results.next()) {
            userId = results.getInt("id");
            userName = results.getString("user_name");
            password = results.getString("password");
        }

        user = new User(userName, password);
        user.setId(userId);

        statement.close();
        results.close();

        return user;
    }

    @Override
    public User find(int id) throws SQLException {
        User user;
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM users WHERE id = ?;");
        statement.setInt(1, id);

        ResultSet results = statement.executeQuery();

        String userName = "";
        String password = "";

        while (results.next()) {
            userName = results.getString("user_name");
            password = results.getString("password");
        }

        user = new User(userName, password);
        user.setId(id);

        statement.close();
        results.close();

        return user;
    }


    @Override
    public void remove(int id) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM users WHERE id=?;");
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = getConn().prepareStatement("SELECT id FROM users;");
        ResultSet results = statement.executeQuery();

        while (results.next()) {

            int id = results.getInt("id");
            users.add(find(id));

        }

        statement.close();
        results.close();

        return users;
    }

    @Override
    public void removeAll() throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("TRUNCATE users CASCADE;");
        statement.executeUpdate();
        statement.close();
    }

}
