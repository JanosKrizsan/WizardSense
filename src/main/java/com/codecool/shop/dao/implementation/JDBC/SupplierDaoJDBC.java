package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC extends ConnectionHandler implements GenericQueriesDao<Supplier> {

    private static SupplierDaoJDBC instance = null;
    private PreparedStatement statement;

    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        try {
            statement = getConn().prepareStatement("INSERT INTO suppliers (name, description) VALUES  (?, ?);");
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getDescription());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public Supplier find(int id) {
        Supplier supplier = null;
        try {
            statement = getConn().prepareStatement("SELECT * FROM suppliers WHERE id = ?;");
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();

            String name = "";
            String description = "";

            while (results.next()){
                name = results.getString("name");
                description = results.getString("description");
            }

            supplier = new Supplier(name, description);
            supplier.setId(id);

            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return supplier;
    }

    @Override
    public void remove(int id) {
        try {
            statement = getConn().prepareStatement("DELETE FROM suppliers WHERE id=?;");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public List<Supplier> getAll() {
        List<Supplier> suppliers = new ArrayList<>();
        try {
            statement = getConn().prepareStatement("SELECT id FROM suppliers;");
            ResultSet results = statement.executeQuery();

            while (results.next()) {

                int id = results.getInt("id");
                suppliers.add(find(id));

            }

            statement.close();
            results.close();

            return suppliers;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return suppliers;
    }

    @Override
    public void removeAll() {
        try {
            statement = getConn().prepareStatement("TRUNCATE suppliers CASCADE ");
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
