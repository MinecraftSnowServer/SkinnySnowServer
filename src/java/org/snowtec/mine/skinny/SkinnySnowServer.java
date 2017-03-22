package org.snowtec.mine.skinny;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Davy
 */
public class SkinnySnowServer extends JavaPlugin {
    private static SkinnySnowServer sInstance;

    private Connection mConnection;

    @Override
    public void onEnable() {
        sInstance = this;
        saveDefaultConfig();

        try {
            final ConfigurationSection config = getConfig().getConfigurationSection("database");
            mConnection = openConnection(config.getString("host"), config.getString("port"),
                    config.getString("database"),
                    config.getString("username"), config.getString("password"));
        } catch (ClassNotFoundException | SQLException e) {
            mConnection = null;
            getLogger().warning("Cannot connect to MySQL server.");
            e.printStackTrace();

            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static SkinnySnowServer getInstance() {
        return sInstance;
    }

    public Connection getConnection() {
        return mConnection;
    }

    private Connection openConnection(final String host, final String port, final String database,
                                     final String username, final String password)
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database, username, password);
    }
}
