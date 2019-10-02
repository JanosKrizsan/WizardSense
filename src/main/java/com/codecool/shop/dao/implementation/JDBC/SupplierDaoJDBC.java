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

    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("INSERT INTO suppliers (name, description) VALUES  (?, ?) RETURNING id;");
        statement.setString(2, supplier.getDescription());
        statement.setString(1, supplier.getName());
        ResultSet result = statement.executeQuery();
        int cartId = supplier.getId();
        while (result.next()) {
            cartId = result.getInt("id");
        }
        supplier.setId(cartId);

        statement.close();
    }

    @Override
    public Supplier find(int id) throws SQLException {
        Supplier supplier;
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM suppliers WHERE id = ?;");
        statement.setInt(1, id);

        ResultSet results = statement.executeQuery();

        String name = "";
        String description = "";

        while (results.next()) {
            name = results.getString("name");
            description = results.getString("description");
        }

        supplier = new Supplier(name, description);
        supplier.setId(id);

        statement.close();
        return supplier;
    }

    @Override
    public void remove(int id) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM suppliers WHERE id=?;");
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public List<Supplier> getAll() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        PreparedStatement statement = getConn().prepareStatement("SELECT id FROM suppliers;");
        ResultSet results = statement.executeQuery();

        while (results.next()) {
            int id = results.getInt("id");
            suppliers.add(find(id));
        }

        results.close();
        statement.close();
        return suppliers;
    }

    @Override
    public void removeAll() throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("TRUNCATE suppliers CASCADE ");
        statement.executeUpdate();
        statement.close();
    }
}
