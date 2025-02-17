package rs.tridanwebshop.tridan.models.articles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Brendovus implements Serializable {


    @SerializedName("ArtikalBrendId")
    @Expose
    private Integer ArtikalBrendId;
    @SerializedName("BrendIme")
    @Expose
    private String BrendIme;
    @SerializedName("BrendLink")
    @Expose
    private String BrendLink;
    @SerializedName("BrendSlika")
    @Expose
    private String BrendSlika;

    /**
     * @return The ArtikalBrendId
     */
    public Integer getArtikalBrendId() {
        return ArtikalBrendId;
    }

    /**
     * @param ArtikalBrendId The ArtikalBrendId
     */
    public void setArtikalBrendId(Integer ArtikalBrendId) {
        this.ArtikalBrendId = ArtikalBrendId;
    }

    /**
     * @return The BrendIme
     */
    public String getBrendIme() {
        return BrendIme;
    }

    /**
     * @param BrendIme The BrendIme
     */
    public void setBrendIme(String BrendIme) {
        this.BrendIme = BrendIme;
    }

    /**
     * @return The BrendLink
     */
    public String getBrendLink() {
        return BrendLink;
    }

    /**
     * @param BrendLink The BrendLink
     */
    public void setBrendLink(String BrendLink) {
        this.BrendLink = BrendLink;
    }

    /**
     * @return The BrendSlika
     */
    public String getBrendSlika() {
        return BrendSlika;
    }

    /**
     * @param BrendSlika The BrendSlika
     */
    public void setBrendSlika(String BrendSlika) {
        this.BrendSlika = BrendSlika;
    }

}
