package com.codecool.shop.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;


public abstract class ConnectionHandler {



    private Connection conn;
    private PGSimpleDataSource dataSource;

    public ConnectionHandler() {
        try {
            this.dataSource = connect();
            this.conn = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public PGSimpleDataSource connect() throws SQLException {
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

    public PGSimpleDataSource getDataSource() {
        return dataSource;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setDataSource(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }
}


