package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class Polsek {
    public Integer id;
    public Integer polres_id;
    public String nama;
    public String singkatan;

    public Polsek() {
    }

    public Polsek(Integer id, Integer polres_id, String nama, String singkatan) {
        this.id = id;
        this.polres_id = polres_id;
        this.nama = nama;
        this.singkatan = singkatan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPolres_id() {
        return polres_id;
    }

    public void setPolres_id(Integer polres_id) {
        this.polres_id = polres_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSingkatan() {
        return singkatan;
    }

    public void setSingkatan(String singkatan) {
        this.singkatan = singkatan;
    }
}
