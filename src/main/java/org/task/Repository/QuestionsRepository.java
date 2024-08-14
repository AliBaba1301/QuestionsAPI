package org.task.Repository;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.task.Model.Question;

import java.util.List;
import java.util.Objects;

@Repository("questionsRepository")
public class QuestionsRepository {
    NamedParameterJdbcTemplate jdbcTemplate;

    public QuestionsRepository() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:myDb");
        ds.setUser("sa");
        ds.setPassword("password");

        jdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }


    public List<Question> getAllQuestions(String disease) {
        String getAllQuestionsSql;
        if(Objects.equals(disease, "all")){
            getAllQuestionsSql = "Select ID,Question, Expected_Answer, Rejection_String" +
                    " From Questions";
        }
        else {
            getAllQuestionsSql = "Select ID,Question, Expected_Answer, Rejection_String" +
                    " From Questions Where Disease in ('generic', :disease)";
        }

        MapSqlParameterSource params = new MapSqlParameterSource("disease", disease);

        return jdbcTemplate.query(getAllQuestionsSql, params, (rs,rowNum) ->
                new Question(rs.getInt("ID"),
                        rs.getString("Question"),
                        rs.getBoolean("Expected_Answer"),
                        rs.getString("Rejection_String")));

    }
}
