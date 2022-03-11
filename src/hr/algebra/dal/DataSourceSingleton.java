/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import hr.algebra.utils.JndiUtils;
import java.util.Properties;
import javax.sql.DataSource;

/**
 *
 * @author RnaBo
 */
public final class DataSourceSingleton {

    private static final String FILE_LOCATION = "d:\\";
    private static String databaseServerName;
    private static String databaseDbName;
    private static String databaseUserName;
    private static String databasePassword;

    private DataSourceSingleton() {
    }

    private static DataSource instance;

    public static DataSource getInstance() {
        if (instance == null) {
            loadProperties();
            instance = createInstance();
        }
        return instance;
    }

    private static void loadProperties() {
        Properties appProps = JndiUtils.readConfigFile(FILE_LOCATION);
        databaseServerName = appProps.getProperty("server");
        databaseDbName = appProps.getProperty("db.database");
        databaseUserName = appProps.getProperty("db.user");
        databasePassword = appProps.getProperty("db.password");
    }

    private static DataSource createInstance() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName(databaseServerName);
        dataSource.setDatabaseName(databaseDbName);
        dataSource.setUser(databaseUserName);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

}
