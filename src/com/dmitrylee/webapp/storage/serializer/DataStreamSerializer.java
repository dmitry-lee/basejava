package com.dmitrylee.webapp.storage.serializer;

import com.dmitrylee.webapp.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void writeResume(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeCollection(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeCollection(dos, r.getSections().entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                AbstractSection section = entry.getValue();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) section).getText());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeCollection(dos, ((ListSection) section).getList(), dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeCollection(dos, ((OrganizationSection) section).getOrganizationList(), organization -> {
                            Link link = organization.getLink();
                            dos.writeUTF(link.getName());
                            String url = link.getUrl();
                            dos.writeUTF(url == null ? "" : url);
                            writeCollection(dos, organization.getExperienceList(), experience -> {
                                dos.writeUTF(experience.getTitle());
                                writeYearMonth(dos, experience.getPeriodFrom());
                                writeYearMonth(dos, experience.getPeriodTo());
                                String description = experience.getDescription();
                                dos.writeUTF(description == null ? "" : description);
                            });
                        });
                        break;
                }
            });
        }
    }

    private void writeYearMonth(DataOutputStream dos, YearMonth yearMonth) throws IOException {
        dos.writeInt(yearMonth.getYear());
        dos.writeInt(yearMonth.getMonthValue());
    }

    private YearMonth readYearMonth(DataInputStream dis) throws IOException {
        return YearMonth.of(dis.readInt(), dis.readInt());
    }

    @Override
    public Resume readResume(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readCollection(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readCollection(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(readList(dis, dis::readUTF));
            case EXPERIENCE:
            case EDUCATION:
                return new OrganizationSection(
                        readList(dis, () -> {
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            return new Organization(
                                    name, url.equals("") ? null : url, readList(dis, () -> {
                                String title = dis.readUTF();
                                YearMonth periodFrom = readYearMonth(dis);
                                YearMonth periodTo = readYearMonth(dis);
                                String description = dis.readUTF();
                                return new Organization.Experience(
                                        title, periodFrom, periodTo, description.equals("") ? null : description);
                            }));
                        }));
            default:
                throw new IllegalStateException();
        }
    }

    private <T> List<T> readList(DataInputStream dis, Reader<T> reader) throws IOException {
        int size = dis.readInt();
        List<T> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(reader.read());
        }
        return result;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T element : collection) {
            writer.write(element);
        }
    }

    private <T> void readCollection(DataInputStream dis, Processor<T> processor) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            processor.process();
        }
    }

    @FunctionalInterface
    private interface Processor<T> {
        void process() throws IOException;
    }

    @FunctionalInterface
    private interface Reader<T> {
        T read() throws IOException;
    }

    @FunctionalInterface
    private interface Writer<T> {
        void write(T t) throws IOException;
    }
}
