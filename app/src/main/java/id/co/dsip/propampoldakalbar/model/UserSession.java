package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class UserSession {
    public String id;
    public String nama;
    public String nrp;
    public String hp;
    public String pas_foto;
    public String foto_kta;
    public String token;
    public String fcm_key;
    public Pangkat pangkat;
    public Jabatan jabatan;
    public User user;
    public Penempatan penempatan;

    public UserSession() {
    }

    public UserSession(String id, String nama, String nrp, String hp, String pas_foto, String foto_kta, String token, String fcm_key, Pangkat pangkat, Jabatan jabatan, User user, Penempatan penempatan) {
        this.id = id;
        this.nama = nama;
        this.nrp = nrp;
        this.hp = hp;
        this.pas_foto = pas_foto;
        this.foto_kta = foto_kta;
        this.token = token;
        this.fcm_key = fcm_key;
        this.pangkat = pangkat;
        this.jabatan = jabatan;
        this.user = user;
        this.penempatan = penempatan;
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

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
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

    public String getFoto_kta() {
        return foto_kta;
    }

    public void setFoto_kta(String foto_kta) {
        this.foto_kta = foto_kta;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getFcm_key() {
        return fcm_key;
    }

    public void setFcm_key(String fcm_key) {
        this.fcm_key = fcm_key;
    }

    public Pangkat getPangkat() {
        return pangkat;
    }

    public void setPangkat(Pangkat pangkat) {
        this.pangkat = pangkat;
    }

    public Jabatan getJabatan() {
        return jabatan;
    }

    public void setJabatan(Jabatan jabatan) {
        this.jabatan = jabatan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Penempatan getPenempatan() {
        return penempatan;
    }

    public void setPenempatan(Penempatan penempatan) {
        this.penempatan = penempatan;
    }
}
