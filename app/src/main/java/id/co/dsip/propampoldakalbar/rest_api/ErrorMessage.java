package id.co.dsip.propampoldakalbar.rest_api;

/**
 * Created by japra_awok on 13/03/2017.
 */

public class ErrorMessage {
    public String field, message;

    public ErrorMessage() {
    }

    public ErrorMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
