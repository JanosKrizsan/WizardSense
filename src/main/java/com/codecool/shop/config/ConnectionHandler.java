package com.codecool.shop.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;


public abstract class ConnectionHandler {



    private Connection conn;
    private DataSource dataSource;

    public ConnectionHandler() {
        try {
            this.dataSource = connect();
            this.conn = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setDatabaseName("wizardsense");
        dataSource.setUser("postgres");
        dataSource.setPassword("19980114");

        dataSource.getConnection().close();

        return dataSource;
    }

    public Connection getConn() {
        return conn;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
