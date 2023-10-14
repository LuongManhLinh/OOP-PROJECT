package com.example.dictionaryjavafxtest.ExperimentGameClasses;

public class Result {
    private String question;
    private String yourAnswer;
    private String trueAnswer;

    public Result() {
        question = "";
        yourAnswer = "";
        trueAnswer = "";
    }

    public Result(String question, String yourAnswer, String trueAnswer) {
        this.question = question;
        this.trueAnswer = trueAnswer;
        this.yourAnswer = yourAnswer;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }
}
