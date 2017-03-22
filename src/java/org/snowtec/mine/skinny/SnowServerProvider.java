package org.snowtec.mine.skinny;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tw.davy.minecraft.skinny.SignedSkin;
import tw.davy.minecraft.skinny.providers.Provider;

/**
 * @author Davy
 */
public class SnowServerProvider implements Provider {
    private static final String QUERY = "SELECT username, mojang_skin, mojang_skin_signature " +
            "FROM authme WHERE `username` = ?";

    @Override
    public SignedSkin getSkinData(final String username) {
        if (getConnection() == null)
            return null;

        try {
            final PreparedStatement statement = getConnection().prepareStatement(QUERY);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                final String value = results.getString("mojang_skin");
                final String signature = results.getString("mojang_skin_signature");

                if (value != null && signature != null &&
                        !value.isEmpty() && !signature.isEmpty())
                    return new SignedSkin(value, signature);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Connection getConnection() {
        return SkinnySnowServer.getInstance().getConnection();
    }
}
