package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class Berita {
    public String id;
    public String judul;
    public String deskripsi;
    public Double accuracy;
    public Double lat;
    public Double lng;
    public String provider;
    public Integer speed;
    public String created_at;
    public CreatorData creator;
    public String updated_at;
    public String updated_by;
    public Jenis jenis;
    public List<Attachment> trnBeritaAttachments = null;
    public List<Coment> trnBeritaComents = null;
    public List<Like> trnBeritaLikes = null;

    public Berita() {
    }

    public Berita(String id, String judul, String deskripsi, Double accuracy, Double lat, Double lng, String provider, Integer speed, String created_at, CreatorData creator, String updated_at, String updated_by, Jenis jenis, List<Attachment> trnBeritaAttachments, List<Coment> trnBeritaComents, List<Like> trnBeritaLikes) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.accuracy = accuracy;
        this.lat = lat;
        this.lng = lng;
        this.provider = provider;
        this.speed = speed;
        this.created_at = created_at;
        this.creator = creator;
        this.updated_at = updated_at;
        this.updated_by = updated_by;
        this.jenis = jenis;
        this.trnBeritaAttachments = trnBeritaAttachments;
        this.trnBeritaComents = trnBeritaComents;
        this.trnBeritaLikes = trnBeritaLikes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public CreatorData getCreator() {
        return creator;
    }

    public void setCreator(CreatorData creator) {
        this.creator = creator;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public Jenis getJenis() {
        return jenis;
    }

    public void setJenis(Jenis jenis) {
        this.jenis = jenis;
    }

    public List<Attachment> getTrnBeritaAttachments() {
        return trnBeritaAttachments;
    }

    public void setTrnBeritaAttachments(List<Attachment> trnBeritaAttachments) {
        this.trnBeritaAttachments = trnBeritaAttachments;
    }

    public List<Coment> getTrnBeritaComents() {
        return trnBeritaComents;
    }

    public void setTrnBeritaComents(List<Coment> trnBeritaComents) {
        this.trnBeritaComents = trnBeritaComents;
    }

    public List<Like> getTrnBeritaLikes() {
        return trnBeritaLikes;
    }

    public void setTrnBeritaLikes(List<Like> trnBeritaLikes) {
        this.trnBeritaLikes = trnBeritaLikes;
    }
}
