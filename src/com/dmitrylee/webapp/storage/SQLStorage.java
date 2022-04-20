package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.*;
import com.dmitrylee.webapp.sql.SQLHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid=?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            saveContacts(resume, conn);
            saveSections(resume, conn);
            return null;
        });
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
        return sqlHelper.executeStatement("SELECT * FROM resume ORDER BY full_name, uuid", psResume -> {
            ResultSet resumeRS = psResume.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (resumeRS.next()) {
                Resume r = new Resume(resumeRS.getString("uuid"), resumeRS.getString("full_name"));
                sqlHelper.executeStatement("SELECT * FROM contact WHERE resume_uuid=?", psContacts -> {
                    psContacts.setString(1, r.getUuid());
                    ResultSet contactsRS = psContacts.executeQuery();
                    if (contactsRS.next()) {
                        readContacts(r, contactsRS);
                    }
                    readSections(r);
                    return null;
                });
                resumes.add(r);
            }
            return resumes;
        });
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
            return null;
        });
    }

    private void saveSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                AbstractSection section = e.getValue();
                String value = null;
                if (section instanceof TextSection) {
                    value = ((TextSection) section).getText();
                } else if (section instanceof ListSection) {
                    value = String.join("\n", ((ListSection) section).getList());
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
