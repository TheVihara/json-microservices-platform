package net.unnamed.common.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.config.YamlConfig;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class MySqlConfig extends YamlConfig<MySqlConfig> {

    String driver = "com.mysql.jdbc.Driver";

    String url = "jdbc:mysql://localhost:3306/minecraft";

    String username = "root";

    String password = "password";
}
