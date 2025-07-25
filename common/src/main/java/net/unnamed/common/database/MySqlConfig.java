package net.unnamed.common.database;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class MySqlConfig extends ConfigurablePojo<MySqlConfig> {
    @Key("driver")
    String driver = "com.mysql.jdbc.Driver";

    @Key("url")
    String url = "jdbc:mysql://localhost:3306/minecraft";

    @Key("username")
    String username = "root";

    @Key("password")
    String password = "password";
}
