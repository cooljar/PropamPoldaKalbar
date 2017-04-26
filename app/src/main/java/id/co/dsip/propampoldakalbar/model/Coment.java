package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class Coment {
    public String id;
    public String created_at;
    public String coment;
    public CreatorData creator;

    public Coment() {
    }

    public Coment(String id, String created_at, String coment, CreatorData creator) {
        this.id = id;
        this.created_at = created_at;
        this.coment = coment;
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public CreatorData getCreator() {
        return creator;
    }

    public void setCreator(CreatorData creator) {
        this.creator = creator;
    }
}
