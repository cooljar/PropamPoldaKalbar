package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class Masyarakat {
    public String id;
    public String nama;
    public String hp;
    public String pas_foto;
    public String token;
    public String fcm_key;

    public Masyarakat() {
    }

    public Masyarakat(String id, String nama, String hp, String pas_foto, String token, String fcm_key) {
        this.id = id;
        this.nama = nama;
        this.hp = hp;
        this.pas_foto = pas_foto;
        this.token = token;
        this.fcm_key = fcm_key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getPas_foto() {
        return pas_foto;
    }

    public void setPas_foto(String pas_foto) {
        this.pas_foto = pas_foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFcm_key() {
        return fcm_key;
    }

    public void setFcm_key(String fcm_key) {
        this.fcm_key = fcm_key;
    }
}
