package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;
import com.dmitrylee.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLStorage implements Storage {

    public interface SQLHelper<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public final ConnectionFactory connectionFactory;

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private <T> T executeStatement(String sqlStatement, SQLHelper<T> sqlHelper) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlStatement)) {
            return sqlHelper.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void clear() {
        executeStatement("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume resume) {
        executeStatement("UPDATE resume SET full_name=? WHERE uuid=?", ps -> {
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        executeStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return executeStatement("SELECT * FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        executeStatement("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (!ps.execute()) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return executeStatement("SELECT * FROM resume ORDER BY full_name", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return executeStatement("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        });
    }
}
