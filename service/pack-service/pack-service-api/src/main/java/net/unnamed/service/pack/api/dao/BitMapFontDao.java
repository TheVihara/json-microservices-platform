package net.unnamed.service.pack.api.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.unnamed.common.database.dao.DaoCrud;
import net.unnamed.service.pack.api.BitMapFont;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BitMapFontDao implements DaoCrud<BitMapFont, String> {
    DataSource dataSource;

    @Override
    public CompletableFuture<Boolean> init() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = """
                CREATE TABLE IF NOT EXISTS bitmap_fonts (
                    `key` VARCHAR(255) PRIMARY KEY,
                    `font` TEXT NOT NULL,
                    `width` INT NOT NULL
                );
            """;
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<BitMapFont> getById(String key) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM bitmap_fonts WHERE `key` = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, key);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new BitMapFont(
                                rs.getString("key"),
                                rs.getString("font"),
                                rs.getInt("width")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<List<BitMapFont>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            List<BitMapFont> fonts = new ArrayList<>();
            String sql = "SELECT * FROM bitmap_fonts";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fonts.add(new BitMapFont(
                            rs.getString("key"),
                            rs.getString("font"),
                            rs.getInt("width")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return fonts;
        });
    }

    @Override
    public CompletableFuture<BitMapFont> save(BitMapFont bitMapFont) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = """
                INSERT INTO bitmap_fonts (`key`, `font`, `width`)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE font = VALUES(font), width = VALUES(width)
            """;
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bitMapFont.getKey());
                stmt.setString(2, bitMapFont.getFont());
                stmt.setInt(3, bitMapFont.getWidth());
                stmt.executeUpdate();
                return bitMapFont;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(String key) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "DELETE FROM bitmap_fonts WHERE `key` = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, key);
                int affected = stmt.executeUpdate();
                return affected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
