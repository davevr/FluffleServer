package com.eweware.fluffle.obj;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

/**
 * Created by davidvronay on 5/10/16.
 */

@Entity
@Index
public class BunnyFurColorObj {
    @Id
    public Long id;
    public String ColorName;
    public int rarity;
    public transient long parentBreedId;

    //@Ignore
    public List<BunnyEyeColorObj> possibleEyeColors;

    public BunnyFurColorObj(String furColor, int chance) {
        ColorName = furColor;
        rarity = chance;
    }

    public BunnyFurColorObj() {

    }
}
