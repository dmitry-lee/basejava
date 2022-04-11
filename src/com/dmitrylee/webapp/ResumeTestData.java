package com.dmitrylee.webapp;

import com.dmitrylee.webapp.model.*;

import java.time.YearMonth;
import java.util.*;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume resume = getResumeWithTestData("uuid1", "Григорий Кислин");

        System.out.println(resume.getFullName());
        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
            System.out.println(entry.getKey().getTitle() + ": " + entry.getValue());
        }

        for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
            System.out.println(entry.getKey().getTitle());
            System.out.println(entry.getValue().toString());
        }
    }

    public static void fillData(Resume resume) {
        Map<ContactType, String> contacts = resume.getContacts();
        contacts.put(ContactType.TELEPHONE, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "grigory.kislin");
        contacts.put(ContactType.EMAIL, "gkislin@yandex.ru");
        contacts.put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        contacts.put(ContactType.GITHUB, "https://github.com/gkislin");
        contacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        contacts.put(ContactType.HOMEPAGE, "http://gkislin.ru/");
        Map<SectionType, AbstractSection> sections = resume.getSections();
        sections.put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        sections.put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        ListSection achievements = new ListSection(Arrays.asList("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.", "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.", "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.", "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.", "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).", "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа."));
        sections.put(SectionType.ACHIEVEMENT, achievements);

        ListSection qualification = new ListSection(Arrays.asList("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2", "Version control: Subversion, Git, Mercury, ClearCase, Perforce", "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle", "MySQL, SQLite, MS SQL, HSQLDB", "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy", "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts"));
        sections.put(SectionType.QUALIFICATIONS, qualification);

        List<Organization> experience = new ArrayList<>();
        experience.add(new Organization("Java Online Projects", "http://javaops.ru/", Collections.singletonList(new Organization.Experience("Автор проекта.", YearMonth.of(2013, 10), YearMonth.now(), "Создание, организация и проведение Java онлайн проектов и стажировок."))));

        experience.add(new Organization("Wrike", "https://www.wrike.com/", Collections.singletonList(new Organization.Experience("Старший разработчик (backend)", YearMonth.of(2013, 10), YearMonth.of(2014, 10), "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO./**/"))));

        sections.put(SectionType.EXPERIENCE, new OrganizationSection(experience));

        List<Organization> education = new ArrayList<>();
        education.add(new Organization("Coursera", "https://www.coursera.org/course/progfun", Collections.singletonList(new Organization.Experience("\"Functional Programming Principles in Scala\" by Martin Odersky", YearMonth.of(2013, 3), YearMonth.of(2013, 5), null))));

        education.add(new Organization("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "http://www.ifmo.ru/", Arrays.asList(new Organization.Experience("Аспирантура (программист С, С++)", YearMonth.of(1993, 9), YearMonth.of(1996, 7), null), new Organization.Experience("Инженер (программист Fortran, C)", YearMonth.of(1987, 9), YearMonth.of(1993, 7), null))));
        sections.put(SectionType.EDUCATION, new OrganizationSection(education));
    }

    public static Resume getResumeWithTestData(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        //fillData(resume);
        return resume;
    }

    public static Resume getTestResumeWithNullValues(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        //fillData(resume);
        for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
            if (entry.getKey().equals(SectionType.EDUCATION)) {
                Organization organization = ((OrganizationSection) entry.getValue()).getOrganizationList().get(0);
                organization.getLink().setUrl(null);
                organization.getExperienceList().get(0).setDescription(null);
            }
        }
        return resume;
    }
}
