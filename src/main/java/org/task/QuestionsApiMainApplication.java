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

        ConfigurableApplicationContext ctx = SpringApplication.run(QuestionsApiMainApplication.class, args);

        Connection conn = DriverManager.getConnection(
                ctx.getEnvironment().getProperty("spring.datasource.url"),
                ctx.getEnvironment().getProperty("spring.datasource.username"),
                ctx.getEnvironment().getProperty("spring.datasource.password"));
        conn.setAutoCommit(true);

        Statement configureQuestionsTable =  conn.createStatement();
        configureQuestionsTable.execute(createSql);
        conn.close();
        }

}
