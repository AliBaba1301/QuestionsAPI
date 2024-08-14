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
                "('generic', 'Are you pregnant, breastfeeding or planning a pregnancy?',False,'This treatment may not suitable for you. " +
                "Please speak to your GP for any further advice')," +
                "('allergy', 'Do you understand there is a possibility that oral antihistamines may cause drowsiness and affect driving or operating machinery?',True,'Please " +
                "speak to our helpline for more information')," +
                "('allergy', 'Have you previously experienced an allergic reaction to antihistamines?',False, " +
                "'Please speak to your GP about medications that are suitable for you')," +
                "('allergy', 'Do you understand that you should not take: Antacids or emergency contraception 2 hours before or after taking an antihistamine?'," +
                "True,'Please speak to to our helpline for more information')," +
                "('allergy', 'Do you have any history of heart issues?',False,'This treatment is not suitable for you. " +
                "Please speak to your GP for any further advice')";

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
