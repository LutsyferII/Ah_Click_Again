package com.zarj.ahClickAgain;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.zarj.ahClickAgain.Dungeon.Dungeon;
import com.zarj.ahClickAgain.Dungeon.DungeonLevel;
import com.zarj.ahClickAgain.Dungeon.DungeonManager;
import com.zarj.ahClickAgain.Dungeon.enemys.Enemy;
import com.zarj.ahClickAgain.items.Inventory;
import com.zarj.ahClickAgain.items.effects.LiveEffect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player implements Serializable {

    static Weapon weapon = new Sword(5,"palka","ai_sword_4", "start");
    static List<String> weapon_has = new ArrayList<>();
    private static final long serialVersionUID = 2L;
    static long coins = 0L;
    static long diamond = 0;
    static long dps = 1;
    static DungeonLevel on_level;
    static String  coins_text = coins+"";
    static Enemy enemy;
    static long damage = weapon.getDamage();
    static int rebirthes = 0;
    static long reb_coins = 0;
    static long damage_buff = 0;
    static int perc_damage_buff = 0;
    static long dps_buff = 0;
    static int perc_dps_buff  = 0;
    static int coins_buff = 0;
    static HashMap<String, LiveEffect> effects = new HashMap<>();



    public static void createFromNone(){
        on_level = DungeonManager.getFirstDung().getFirstLevel();
        enemy = on_level.rollEnemy();
    }


    public static void saveInSharedPer(@NonNull SharedPreferences.Editor editor){

        editor.putLong("damage", damage);
        editor.putInt("rebirth", rebirthes);
        editor.putLong("reb_coins",reb_coins);
        editor.putLong("coins",coins);
        editor.putLong("diamonds",diamond);
        editor.putInt("exp",on_level.getExp());
        editor.putString("level",on_level.getFullId());
        editor.putString("weapon",weapon.getId());
        editor.putStringSet("weapon_has",new HashSet<>(weapon_has));
        editor.putLong("dps",dps);
        editor.commit();
        PetManager.savePref();
        BonusManager.savePeref();
        Inventory.save(editor);



    }

    public static String getTextCoins(){
        coins_text = coins+"";
        return coins_text;
    }

    public static void loadFromSharedPer(@NonNull SharedPreferences sp){

        coins =sp.getLong("coins",0);
        coins_text = coins+"";
        diamond = sp.getLong("diamonds",0);
        int exp = sp.getInt("exp",0);

        String sword_id = sp.getString("weapon","0");
        weapon = WeaponManager.getWeaponOrFirst(sword_id);

        rebirthes = sp.getInt("rebirth",0);
        reb_coins = sp.getLong("reb_coins", 0);

        String dung_level = sp.getString("level","first:0");

        Set<String> set_has_w = sp.getStringSet("weapon_has",new HashSet<String>());
        if(!set_has_w.isEmpty()){
            weapon_has.clear();
            weapon_has.addAll(set_has_w);
        }




        String[] dung_parts = dung_level.split(":");
        if(dung_parts.length<2){
            on_level = DungeonManager.getFirstDung().getFirstLevel().clone();
        }else{
            int level_id = Integer.parseInt(dung_parts[1]);
            Dungeon dung = DungeonManager.getDungeon(dung_parts[0]);
            if(dung==null){

                System.out.println("LOAD DUND LEVEL ERROR");
                on_level = DungeonManager.getFirstDung().getFirstLevel().clone();
            }else{
                on_level = dung.getLevelOrFirst(level_id).clone();
            }
        }
        on_level.setExp(exp);
        enemy = on_level.rollEnemy();
        PetManager.loadPref();
        BonusManager.loadPered();
        updateBonuses();
        updateDPS();
        updateDamage();
        Inventory.load(sp);

    }



    public static void clear(){
        damage = 0;
        damage_buff = 0;
        perc_damage_buff = 0;
        dps = 0;
        dps_buff = 0;
        perc_dps_buff = 0;
        coins_buff = 0;

    }


    public static void setWeapon(Weapon w){
        weapon = w;
    }
    public static long getCoins(){
        return coins;
    }
    public static boolean hasEnoughMoney(long price){
        return !(price>coins);
    }
    public static long addCoins(long coin){

        coins+=coin;
        if(coins_buff>0){
            coins+=(coin*(coins_buff/100));
        }
        return coins;
    }

    public static long getDiamond(){
        return diamond;
    }
    public static long giveDiamonds(long dmd){
        diamond+=dmd;
        return diamond;
    }
    public static boolean hasEnoughDiamonds(long dmd){
        return (diamond>=dmd);
    }

    public static long decrDiamonds(long dmd){
        diamond-=dmd;
        return diamond;
    }
    public static Long decrCoins(Long coin){
        coins-=coin;
        return coins;
    }
    public static DungeonLevel where(){
        return on_level;
    }

    public static void setEnemy(Enemy enemy) {
        enemy = enemy;
    }

    public static Enemy getEnemy() {
        return enemy;
    }
    public static boolean hasWeapon(String id){
        return weapon_has.contains(id);
    }


    public static void letsRebirth(){
        rebirthes++;
        BonusManager.bonuses.get(4).has = true;
        damage = 0;
        damage_buff = 0;
        perc_damage_buff = 0;
        dps = 0;
        dps_buff = 0;
        perc_dps_buff = 0;
        coins_buff = 0;
        weapon = WeaponManager.getFirstWeapon();
        weapon_has.clear();
        reb_coins+= getHowManyGetOnRebirth();
        on_level = DungeonManager.getFirstDung().getFirstLevel().clone();
        enemy = on_level.rollEnemy();

        coins = 0L;

        updateBonuses();
        updateDamage();
        PetManager.rebirth();
        updateDPS();

    }
    public static long getHowManyGetOnRebirth(){
        long reb_coins = (long) (Math.pow(10.0,DungeonManager.getDungIndex(on_level.getDungID().getId()))*2.5);
        reb_coins+=on_level.getId()*(reb_coins/2)+coins/100;
       return  reb_coins;
    }
    public static void updateDamage(){
        damage = weapon.getDamage();
        damage += damage_buff;
        damage+= damage*((float)perc_damage_buff/100);

    }
    public static void updateBonuses(){
        BonusManager.dropUse();
        BonusManager.letsActions();
    }


    public static void updateDPS(){
        dps = 1+PetManager.getAllDPS();
        dps += dps_buff;
        dps += dps* ((float)perc_dps_buff/100);
    }
    public static int getCoinsBuff(){
        return coins_buff;
    }
    public static void setCoinsBuff(int amount){
        coins_buff = amount;
    }
    public static int getPercDmgBuff(){
        return perc_damage_buff;
    }
    public static void setPercDmgBuff(int amount){
        perc_damage_buff = amount;
    }
    public static int getPercDpsBuff(){
        return perc_dps_buff;
    }
    public static void setPercDpsBuff(int amount){
        perc_dps_buff = amount;
    }
    public static void addEffect(LiveEffect effect){
        effects.put(effect.getId(),effect);
    }
    public static boolean hasEffect(String id){
        return effects.containsKey(id);
    }
    public static void removeEffect(String id){
        effects.remove(id);
    }
    public static int effectTime(String id){
        if(effects.containsKey(id)){
            return effects.get(id).timeSec();
        }else{
            return 0;
        }
    }
}

