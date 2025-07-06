package net.unnamed.common.database;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;

public class MySqlConfig extends ConfigurablePojo<MySqlConfig> {
    @Key("driver")
    private String driver = "com.mysql.jdbc.Driver";

    @Key("url")
    private String url = "jdbc:mysql://localhost:3306/minecraft";

    @Key("username")
    private String username = "localhost";

    @Key("password")
    private String password = "password";

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
