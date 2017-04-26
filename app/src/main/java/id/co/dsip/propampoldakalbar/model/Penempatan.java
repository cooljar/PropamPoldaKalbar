package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class Penempatan {
    public Integer id;
    public String name;
    public String code;
    public Polda polda;
    public Polres polres;
    public Polsek polsek;

    public Penempatan() {
    }

    public Penempatan(Integer id, String name, String code, Polda polda, Polres polres, Polsek polsek) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.polda = polda;
        this.polres = polres;
        this.polsek = polsek;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Polda getPolda() {
        return polda;
    }

    public void setPolda(Polda polda) {
        this.polda = polda;
    }

    public Polres getPolres() {
        return polres;
    }

    public void setPolres(Polres polres) {
        this.polres = polres;
    }

    public Polsek getPolsek() {
        return polsek;
    }

    public void setPolsek(Polsek polsek) {
        this.polsek = polsek;
    }
}
