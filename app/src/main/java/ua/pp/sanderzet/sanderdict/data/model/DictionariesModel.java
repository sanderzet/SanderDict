package ua.pp.sanderzet.sanderdict.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DictionariesModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ver")
    @Expose
    private Integer ver;
    @SerializedName("inLang")
    @Expose
    private String inLang;
    @SerializedName("outLang")
    @Expose
    private String outLang;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("size")
    @Expose
    private Double size;
    @SerializedName("URL")
    @Expose
    private String uRL;

    /**
     * No args constructor for use in serialization
     *
     */
    public DictionariesModel() {
    }

    /**
     *
     * @param id
     * @param uRL
     * @param description
     * @param fileName
     * @param outLang
     * @param inLang
     * @param ver
     * @param size
     */
    public DictionariesModel(Integer id, Integer ver, String inLang, String outLang, String description, String fileName, Double size, String uRL) {
        super();
        this.id = id;
        this.ver = ver;
        this.inLang = inLang;
        this.outLang = outLang;
        this.description = description;
        this.fileName = fileName;
        this.size = size;
        this.uRL = uRL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    public String getInLang() {
        return inLang;
    }

    public void setInLang(String inLang) {
        this.inLang = inLang;
    }

    public String getOutLang() {
        return outLang;
    }

    public void setOutLang(String outLang) {
        this.outLang = outLang;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }

}

