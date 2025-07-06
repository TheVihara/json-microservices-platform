package net.unnamed.minecraft.paper.essentials;

import de.bsommerfeld.jshepherd.annotation.Key;
import de.bsommerfeld.jshepherd.core.ConfigurablePojo;
import net.unnamed.common.database.MySqlConfig;

public class EssentialsConfig extends ConfigurablePojo<EssentialsConfig> {
    @Key("mysql")
    private MySqlConfig mySqlConfig = new MySqlConfig();

    public void setMySqlConfig(MySqlConfig mySqlConfig) {
        this.mySqlConfig = mySqlConfig;
    }

    public MySqlConfig getMySqlConfig() {
        return mySqlConfig;
    }
}
