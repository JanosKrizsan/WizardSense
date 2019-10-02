package com.codecool.shop.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.postgresql.ds.PGSimpleDataSource;


public abstract class ConnectionHandler extends ErrorHandling {

    private Connection conn;
    private PGSimpleDataSource dataSource;

    public ConnectionHandler() {
        try {
            this.dataSource = connect();
            this.conn = dataSource.getConnection();
        } catch (SQLException e) {
            ExceptionOccurred(e);
        }
    }

    static public PGSimpleDataSource connect() throws SQLException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        try{
            ClassLoader cl = Class.forName("com.codecool.shop.config.ConnectionHandler").getClassLoader();

            InputStream inputs = cl.getResourceAsStream("datasource.properties");
            if(Utils.isJUnitTest()) {
                inputs = cl.getResourceAsStream("test.properties");
            }

            Properties prop = new Properties();

            if (inputs != null) {
                prop.load(new InputStreamReader(inputs, StandardCharsets.UTF_8));
            }

            dataSource.setURL(prop.getProperty("url"));
            dataSource.setDatabaseName(prop.getProperty("name"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));


        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e);
        }


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
}


