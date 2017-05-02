package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by japra_awok on 24/04/2017.
 */

@Parcel
public class Instruksi {
    public String id, perihal, deskripsi, created_at;
    public CreatorData creator, instruktor;
    public List<Penempatan> penempatans;
    public List<Jabatan> jabatans;
    public List<Polisi> anggota_polisi;

    public Instruksi() {
    }

    public Instruksi(String id, String perihal, String deskripsi, String created_at, CreatorData creator, CreatorData instruktor, List<Penempatan> penempatans, List<Jabatan> jabatans, List<Polisi> anggota_polisi) {
        this.id = id;
        this.perihal = perihal;
        this.deskripsi = deskripsi;
        this.created_at = created_at;
        this.creator = creator;
        this.instruktor = instruktor;
        this.penempatans = penempatans;
        this.jabatans = jabatans;
        this.anggota_polisi = anggota_polisi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPerihal() {
        return perihal;
    }

    public void setPerihal(String perihal) {
        this.perihal = perihal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
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

    public CreatorData getInstruktor() {
        return instruktor;
    }

    public void setInstruktor(CreatorData instruktor) {
        this.instruktor = instruktor;
    }

    public List<Penempatan> getPenempatans() {
        return penempatans;
    }

    public void setPenempatans(List<Penempatan> penempatans) {
        this.penempatans = penempatans;
    }

    public List<Jabatan> getJabatans() {
        return jabatans;
    }

    public void setJabatans(List<Jabatan> jabatans) {
        this.jabatans = jabatans;
    }

    public List<Polisi> getAnggota_polisi() {
        return anggota_polisi;
    }

    public void setAnggota_polisi(List<Polisi> anggota_polisi) {
        this.anggota_polisi = anggota_polisi;
    }
}
