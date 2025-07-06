package net.unnamed.common.database.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.unnamed.common.database.MySqlConfig;

public class MySqlDatabase {
    private final HikariDataSource dataSource;

    public MySqlDatabase(MySqlConfig mySqlConfig) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(mySqlConfig.getDriver());
        config.setJdbcUrl(mySqlConfig.getUrl());
        config.setUsername(mySqlConfig.getUsername());
        config.setPassword(mySqlConfig.getPassword());
        dataSource = new HikariDataSource(config);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
