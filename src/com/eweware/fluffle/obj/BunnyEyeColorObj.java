package com.eweware.fluffle.obj;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by davidvronay on 5/10/16.
 */

@Entity
@Index
public class BunnyEyeColorObj {
    @Id
    public Long id;
    public String ColorName;
    public int rarity;
    public transient long parentFurColorId;

    public BunnyEyeColorObj(String theColor, int chance) {
        ColorName = theColor;
        rarity = chance;

    }

    public BunnyEyeColorObj() {

    }
}
