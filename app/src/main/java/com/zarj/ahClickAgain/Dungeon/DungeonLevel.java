package com.zarj.ahClickAgain.Dungeon;

import com.zarj.ahClickAgain.Dungeon.enemys.Boss;
import com.zarj.ahClickAgain.Dungeon.enemys.EnemyMob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class DungeonLevel {


    String dungID;
    final String id;
    int exp = 0;
    int need_exp;

    String back_id = "back_1_1";

    public List<String> mobs = new ArrayList<>();
    String boss;

    public DungeonLevel(String id,int exp, Collection<String> mobs,String boss, String dungeon){
        this.id = id;
        this.need_exp = exp;
        this.mobs.clear();
        this.mobs.addAll(mobs);
        this.boss = boss;
        this.dungID = dungeon;
    }
    public DungeonLevel(String id,int exp, Collection<String> mobs,String boss, String dungeon,String back_id){
        this.id = id;
        this.need_exp = exp;
        this.mobs.clear();
        this.mobs.addAll(mobs);
        this.boss = boss;
        this.dungID = dungeon;
        this.back_id = back_id;
    }
    public DungeonLevel clone(){
        return new DungeonLevel(id,need_exp,mobs,boss, dungID,back_id);
    }

    public boolean setExp(int exp){
        if(need_exp<=exp){
            this.exp=need_exp;
            return true;
        }else{
            this.exp = exp;
            return false;
        }
    }
    public void setDungeon(String dungeon){
        this.dungID = dungeon;
    }


    public void setBackgroundId(String id){
        this.back_id = id;
    }
    public String getBackgroundId(){
        return back_id;
    }
    public EnemyMob rollEnemy(){

        return DungeonManager.rollMob(mobs);

    }
    public boolean addExp(int exp){
        if(this.need_exp-this.exp<=exp){
            this.exp = this.need_exp;
            return true;
        }else{
            this.exp+=exp;
            return false;
        }
    }
    public DungeonLevel getNextLevel(){
        final Dungeon dung = DungeonManager.getDungeon(dungID);
        if(dung == null){
            System.out.println("Error: dung is not exist");
            return null;
        }
        if(!dung.isLastLevel(this.id)){
            System.out.println("NOT LAST");
            return dung.getNextLevel(id);
        }else if(DungeonManager.hasNextDungeon(dung.id)){
            System.out.println("HAS NEXT");
            return DungeonManager.getNextDung(dung.id).getFirstLevel();
        }else{
            System.out.println("NOT HAS NEXT");
            return null;
        }
    }

    public String getId(){
        return id;
    }
    public String getFullId(){
        return dungID+":"+id;
    }
    public int getExp(){
        return exp;
    }
    public int getNeed_exp(){
        return need_exp;
    }
    public Boss getBoss(){
        return DungeonManager.getBoss(this.boss).clone();
    }

    public boolean checkId(String id){
        return (this.id.equals(id));
    }


}
