package ua.pp.sanderzet.sanderdict.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import ua.pp.sanderzet.sanderdict.data.util.DateConverter;


/**
 * Created by sander on 27/08/17.
 */

@Entity
public class FavoriteModel {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String word;
    public String definition;
@TypeConverters(DateConverter.class)
    public Date dateOfStoring;
    public Integer stars;

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
