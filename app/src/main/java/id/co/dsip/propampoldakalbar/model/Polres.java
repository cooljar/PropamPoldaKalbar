package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class Polres {
    public Integer id;
    public Integer polda_id;
    public String nama;
    public String singkatan;

    public Polres() {
    }

    public Polres(Integer id, Integer polda_id, String nama, String singkatan) {
        this.id = id;
        this.polda_id = polda_id;
        this.nama = nama;
        this.singkatan = singkatan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPolda_id() {
        return polda_id;
    }

    public void setPolda_id(Integer polda_id) {
        this.polda_id = polda_id;
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
