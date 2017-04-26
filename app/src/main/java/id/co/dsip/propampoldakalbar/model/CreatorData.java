package id.co.dsip.propampoldakalbar.model;

import org.parceler.Parcel;

/**
 * Created by japra_awok on 12/04/2017.
 */

@Parcel
public class CreatorData {
    public User user;
    public Polisi polisi;
    public Masyarakat masyarakat;

    public CreatorData() {
    }

    public CreatorData(User user, Polisi polisi, Masyarakat masyarakat) {
        this.user = user;
        this.polisi = polisi;
        this.masyarakat = masyarakat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Polisi getPolisi() {
        return polisi;
    }

    public void setPolisi(Polisi polisi) {
        this.polisi = polisi;
    }

    public Masyarakat getMasyarakat() {
        return masyarakat;
    }

    public void setMasyarakat(Masyarakat masyarakat) {
        this.masyarakat = masyarakat;
    }
}
