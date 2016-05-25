package com.eweware.fluffle.obj;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Created by davidvronay on 5/10/16.
 */

@Entity
@Index
public class GameObj {
    @Id
    public Long id;

    public List<Integer> GrowthStages;

    @Ignore
    public List<BunnyBreedObj> BunnyBreeds;

    @Ignore
    public List<BunnyObj> Store;

    public int StartingCarrots;

    @Ignore
    public transient Random rnd = new Random();

    public transient int BreedChance = 100;


}
