package rs.tridanwebshop.tridan.models.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemUpdateResponse {

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
    @SerializedName("ukupnaKolicina")
    @Expose
    private Integer ukupnaKolicina;

    /**
     * @return The tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag The tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @return The error
     */
    public Integer getError() {
        return error;
    }

    /**
     * @param error The error
     */
    public void setError(Integer error) {
        this.error = error;
    }

    /**
     * @return The errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg The error_msg
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return The ukupnaKolicina
     */
    public Integer getUkupnaKolicina() {
        return ukupnaKolicina;
    }

    /**
     * @param ukupnaKolicina The ukupnaKolicina
     */
    public void setUkupnaKolicina(Integer ukupnaKolicina) {
        this.ukupnaKolicina = ukupnaKolicina;
    }

}
