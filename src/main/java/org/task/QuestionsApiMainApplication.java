package org.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class QuestionsApiMainApplication {
    public static void main(String[] args) throws SQLException {
        String createSql = "CREATE TABLE Questions ( " +
                "ID int AUTO_INCREMENT PRIMARY KEY, " +
                "Disease varchar(255) NOT NULL, " +
                "Question TEXT NOT NULL, " +
                "Expected_Answer boolean NOT NULL, " +
                "Rejection_String TEXT NOT NULL)";

        String populateTableSql = "INSERT INTO QUESTIONS (Disease, Question, Expected_Answer, Rejection_String) VALUES " +
                "('generic', 'Are you aged between 18-65?',True,'This treatment is not suitable for you. Please speak to your GP for " +
                "any further advice')," +
                "('generic', 'Are you pregnant, breastfeeding or planning a pregnancy?',False,'This treatment is not suitable for you. " +
                "Please speak to your GP for any further advice')," +
                "('arthritis', 'Are you seeking treatment for relief of pain and/or inflammation in a joint or muscle?',True,'This " +
                "treatment is only suitable for relief of pain and/or inflammation in a joint or muscle. Please speak to your GP for " +
                "any further advice')," +
                "('arthritis', 'Have you tried over the counter pain relief options such as paracetamol?',True,'This treatment is " +
                "for when over the counter painkillers are not strong enough. Please speak to your GP for any further advice')," +
                "('migraine', 'Do you experience migraines for more than 10 days a month?',False,'This treatment is not suitable for you. " +
                "Please speak to your GP for any further advice')," +
                "('migraine', 'Do your migraines follow a broadly similar pattern each time?',True,'This treatment is not suitable for you. " +
                "Please speak to your GP for any further advice')," +
                "('migraine', 'Do your migraines last less than 4 hours without treatment or last longer than 24 hours?',False,'This treatment " +
                "is not suitable for you. Please speak to your GP for any further advice')";

        ConfigurableApplicationContext ctx = SpringApplication.run(QuestionsApiMainApplication.class, args);

        Connection conn = DriverManager.getConnection(
                ctx.getEnvironment().getProperty("spring.datasource.url"),
                ctx.getEnvironment().getProperty("spring.datasource.username"),
                ctx.getEnvironment().getProperty("spring.datasource.password"));
        conn.setAutoCommit(true);

        Statement configureQuestionsTable =  conn.createStatement();
        configureQuestionsTable.execute(createSql);
        configureQuestionsTable.execute(populateTableSql);
        conn.close();
        }

}
