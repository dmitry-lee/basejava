package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.*;
import com.dmitrylee.webapp.sql.SQLHelper;

import java.sql.*;
import java.util.*;

public class SQLStorage implements Storage {

    private final SQLHelper sqlHelper;

    public SQLStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new StorageException("PostgreSQL driver not found", null, e);
        }
        sqlHelper = new SQLHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executeStatement("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            deleteFrom("contact", resume, conn);
            deleteFrom("section", resume, conn);
            saveContacts(resume, conn);
            saveSections(resume, conn);
            return null;
        });
    }

    private void deleteFrom(String table, Resume resume, Connection conn) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE resume_uuid=?", table);
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            saveContacts(r, conn);
            saveSections(r, conn);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeStatement(
                "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    readContacts(resume, rs);
                    readSections(resume);
                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeStatement("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LinkedHashMap<String, Resume> map = new LinkedHashMap<>();
        sqlHelper.executeStatement("SELECT * FROM resume ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                map.put(uuid, new Resume(uuid, rs.getString("full_name")));
            }
            return null;
        });
        sqlHelper.executeStatement("SELECT * FROM contact ORDER BY resume_uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resume resume = map.get(rs.getString("resume_uuid"));
                resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
            }
            return null;
        });
        sqlHelper.executeStatement("SELECT * FROM section ORDER BY resume_uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resume resume = map.get(rs.getString("resume_uuid"));
                addSection(rs, resume);
            }
            return null;
        });
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return sqlHelper.executeStatement("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("count") : 0;
        });
    }

    private void readContacts(Resume resume, ResultSet rs) throws SQLException {
        do {
            String type = rs.getString("type");
            if (type != null) {
                resume.addContact(ContactType.valueOf(type), rs.getString("value"));
            }
        } while (rs.next());
    }

    private void saveContacts(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void readSections(Resume resume) {
        sqlHelper.executeStatement("SELECT type, value FROM section WHERE resume_uuid =?", ps -> {
            ps.setString(1, resume.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addSection(rs, resume);
            }
            return null;
        });
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        SectionType type = SectionType.valueOf(rs.getString("type"));
        String value = rs.getString("value");
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                resume.addSection(type, new TextSection(value));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                resume.addSection(type, new ListSection(Arrays.asList(value.split("\n"))));
                break;
        }
    }

    private void saveSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                SectionType type = e.getKey();
                ps.setString(2, type.name());
                AbstractSection section = e.getValue();
                String value = null;
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        value = ((TextSection) section).getText();
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        value = String.join("\n", ((ListSection) section).getList());
                        break;
                }
                if (value != null) {
                    ps.setString(3, value);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }
}
