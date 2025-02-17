package rs.tridanwebshop.tridan.models.articles.articles_filtered_by_category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import rs.tridanwebshop.tridan.models.articles.Article;
import rs.tridanwebshop.tridan.models.articles.Brendovus;
import rs.tridanwebshop.tridan.models.categories.category_specification.Spec;


public class ArticlesFilteredByCategory {
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("brendovi")
    @Expose
    private List<Brendovus> brendovi = new ArrayList<Brendovus>();
    @SerializedName("specKateg")
    @Expose
    private List<Spec> specKateg = new ArrayList<Spec>();
    @SerializedName("artikli")
    @Expose
    private List<Article> artikli = new ArrayList<Article>();

    /**
     *
     * @return
     * The tag
     */
    public String getTag() {
        return tag;
    }

    /**
     *
     * @param tag
     * The tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The error
     */
    public Integer getError() {
        return error;
    }

    /**
     *
     * @param error
     * The error
     */
    public void setError(Integer error) {
        this.error = error;
    }

    /**
     *
     * @return
     * The errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     *
     * @param errorMsg
     * The error_msg
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     *
     * @return
     * The brendovi
     */
    public List<Brendovus> getBrendovi() {
        return brendovi;
    }

    /**
     *
     * @param brendovi
     * The brendovi
     */
    public void setBrendovi(List<Brendovus> brendovi) {
        this.brendovi = brendovi;
    }

    /**
     *
     * @return
     * The specKateg
     */
    public List<Spec> getSpecKateg() {
        return specKateg;
    }

    /**
     *
     * @param specKateg
     * The specKateg
     */
    public void setSpecKateg(List<Spec> specKateg) {
        this.specKateg = specKateg;
    }

    /**
     *
     * @return
     * The artikli
     */
    public List<Article> getArtikli() {
        return artikli;
    }

    /**
     *
     * @param artikli
     * The artikli
     */
    public void setArtikli(List<Article> artikli) {
        this.artikli = artikli;
    }

}
