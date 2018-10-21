package ua.pp.sanderzet.sanderdict.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import ua.pp.sanderzet.sanderdict.data.util.DateConverter;


/**
 * Created by sander on 27/08/17.
 */

@Entity (tableName = "fdict")
public class FavoriteModel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String word;
    private String definition;

//@TypeConverters(DateConverter.class)
    private Date dateOfStoring;
    private Integer stars;

    public FavoriteModel(int id, String word, String definition, Date dateOfStoring, Integer stars) {
        this.id = id;
        this.word = word;
        this.definition = definition;
        this.dateOfStoring = dateOfStoring;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public Date getDateOfStoring() {
        return dateOfStoring;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setDateOfStoring(Date dateOfStoring) {
        this.dateOfStoring = dateOfStoring;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getStars() {
        return stars;


    }
}
