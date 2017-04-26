package id.co.dsip.propampoldakalbar.model;

/**
 * Created by japra_awok on 22/04/2017.
 */

public class Chat {
    public Polisi from, to;
    public String message, created_at;
    public boolean isMine;

    public Chat() {
    }

    public Chat(Polisi from, Polisi to, String message, String created_at, boolean isMine) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.created_at = created_at;
        this.isMine = isMine;
    }

    public Polisi getFrom() {
        return from;
    }

    public void setFrom(Polisi from) {
        this.from = from;
    }

    public Polisi getTo() {
        return to;
    }

    public void setTo(Polisi to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
