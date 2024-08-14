package org.task.Repository;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.task.Model.Question;

import java.util.*;

@Repository("questionsRepository")
public class QuestionsRepository {
    NamedParameterJdbcTemplate jdbcTemplate;

    public QuestionsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public Map<Integer, Map.Entry<Boolean,String>>  getExpectedResponses() {
        String getAllQuestionsSql = "Select ID, Expected_Answer, Rejection_String" +
                " From Questions";

        Map<Integer, Map.Entry<Boolean,String>> expectedAnswers = new HashMap<>();
        jdbcTemplate.query(getAllQuestionsSql, new HashMap<>(), (rs,rowNum) ->
                expectedAnswers.put(rs.getInt("ID"), new
                        AbstractMap.SimpleEntry<Boolean,String>(
                                rs.getBoolean("Expected_Answer"),
                        rs.getString("Rejection_String"))));

        return expectedAnswers;
    }

    public List<Question>  addQuestions(String disease, List<Question> questions) {
        String uploadQuestionsSql = "INSERT INTO QUESTIONS (Disease, Question, " +
                "Expected_Answer, Rejection_String) " +
                "VALUES (:disease, :question,:expectedAnswer,:rejectionString)";

        for (Question question : questions) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("disease", disease)
                    .addValue("question", question.getQuestion())
                    .addValue("expectedAnswer", question.isExpectedAnswer())
                    .addValue("rejectionString", question.getRejectionString());

            jdbcTemplate.update(uploadQuestionsSql, params);
        }

        return getAllQuestions(disease);

    }

    public List<Question>  updateQuestions(String disease, List<Question> questions) {
        String uploadQuestionsSql = "UPDATE QUESTIONS " +
                "SET Disease = :disease , Question = :question, " +
                "Expected_Answer = :expectedAnswer, Rejection_String = :rejectionString " +
                "WHERE Id = :questionId";

        for (Question question : questions) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("disease", disease)
                    .addValue("question", question.getQuestion())
                    .addValue("expectedAnswer", question.isExpectedAnswer())
                    .addValue("rejectionString", question.getRejectionString())
                    .addValue("questionId", question.getQuestionId());

            jdbcTemplate.update(uploadQuestionsSql, params);
        }

        return getAllQuestions(disease);
    }

    public void deleteQuestions(List<Question> questions) {
        String deleteQuestionsSql = "DELETE FROM QUESTIONS WHERE Id = :questionsId";

        for (Question question : questions) {
            MapSqlParameterSource params = new MapSqlParameterSource(
                    "questionsId", question.getQuestionId());

            jdbcTemplate.update(deleteQuestionsSql, params);
        }
    }
}
