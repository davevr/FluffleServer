package com.eweware.fluffle.obj;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by davidvronay on 5/10/16.
 */

@Entity
@Index
public class BunnyObj {
    @Id
    public Long id;
    public Long OriginalOwner;
    public Long CurrentOwner;
    public DateTime LastFeedDate;
    public Integer HorizontalLoc;
    public Integer VerticalLoc;
    public Long MotherID;
    public Long FatherID;
    public List<Long> Children;
    public String BunnyName;
    public String BreedName;
    public String EyeColorName;
    public String FurColorName;
    public Long BreedID;
    public Boolean Female;
    public Long FurColorID;
    public Long EyeColorID;
    public int BunnySize;
    public int FeedState;
    public int Price;
    public int TotalShares;
    public DateTime LastBred;
    public Boolean inPark;



}
