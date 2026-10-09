
package com.team.karyera.dto;

public class AskAiResponse {

    private String answer;
    private boolean success;

    public AskAiResponse() {
    }

    public AskAiResponse(String answer, boolean success) {
        this.answer = answer;
        this.success = success;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}