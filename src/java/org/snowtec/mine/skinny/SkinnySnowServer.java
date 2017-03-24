package org.snowtec.mine.skinny;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Davy
 */
public class SkinnySnowServer extends JavaPlugin {
    private static SkinnySnowServer sInstance;

    private Connection mConnection;
    private ConnectionCredential mCredential;

    @Override
    public void onEnable() {
        sInstance = this;
        saveDefaultConfig();

        final ConfigurationSection config = getConfig().getConfigurationSection("database");
        mCredential = new ConnectionCredential(config.getString("host"), config.getString("port"),
                config.getString("database"),
                config.getString("username"), config.getString("password"));
        try {
            mConnection = openConnection();
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
        try {
            if (mConnection == null || mConnection.isClosed()) {
                getLogger().warning("Connection to MySQL server closed. Reconnecting...");
                mConnection = openConnection();
            }
        } catch (ClassNotFoundException | SQLException e) {
            mConnection = null;
            getLogger().warning("Cannot connect to MySQL server.");
            e.printStackTrace();
        }

        return mConnection;
    }

    private Connection openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://" +
                mCredential.host+ ":" + mCredential.port + "/" + mCredential.database,
                mCredential.username, mCredential.password);
    }

    class ConnectionCredential {
        final String host, port, database, username, password;

        ConnectionCredential(final String host, final String port, final String database,
                             final String username, final String password) {
            this.host = host;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
        }
    }
}
