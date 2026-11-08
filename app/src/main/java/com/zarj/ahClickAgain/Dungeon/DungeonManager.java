package com.zarj.ahClickAgain.Dungeon;

import android.content.Context;

import com.zarj.ahClickAgain.Datar;
import com.zarj.ahClickAgain.Dungeon.enemys.Boss;
import com.zarj.ahClickAgain.Dungeon.enemys.EnemyMob;
import com.zarj.ahClickAgain.data.JsonLoader;
import com.zarj.ahClickAgain.data.configs.DungeonConfig;
import com.zarj.ahClickAgain.data.configs.LevelConfig;
import com.zarj.ahClickAgain.utils.RollSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DungeonManager {
    static Random rand = new Random();
    //static List<Dungeon> dungeons = new ArrayList<>();

    static HashMap<String,Dungeon> dungeons = new HashMap();
    static HashMap<String,DungeonLevel> levels = new HashMap();
    static HashMap<String,EnemyMob> mobs = new HashMap();
    static HashMap<String,Boss> bosses = new HashMap();
    public static void create(Context context){
        dungeons.clear();
        mobs.clear();
        bosses.clear();
        for(Map.Entry<String, LinkedHashMap> entry: Datar.getMobsMap().entrySet()){
            mobs.put(entry.getKey(),new EnemyMob(entry.getKey(),entry.getValue()));
            System.out.println("СОЗДАН МОБ: "+entry.getKey());
        }
        for(Map.Entry<String, LinkedHashMap> entry: Datar.getBossMap().entrySet()){
            bosses.put(entry.getKey(),new Boss(entry.getKey(),entry.getValue()));
            System.out.println("СОЗДАН Boss: "+entry.getKey());
        }

        Map<String, LevelConfig> levelsConfig = JsonLoader.INSTANCE.loadLevels(context);
        for (Map.Entry<String, LevelConfig> entry : levelsConfig.entrySet()) {
            String id = entry.getKey();
            LevelConfig config = entry.getValue();

            DungeonLevel level = new DungeonLevel(
                    id,
                    config.getNeedExp(),
                    config.getMobIds(),
                    config.getBossId(),
                    null,
                    config.getBackgroundId()
            );
            levels.put(id, level);
        }

        Map<String, DungeonConfig> dungeonsConfig = JsonLoader.INSTANCE.loadDungeons(context);
        for (Map.Entry<String, DungeonConfig> entry : dungeonsConfig.entrySet()) {
            String id = entry.getKey();
            DungeonConfig config = entry.getValue();
            Dungeon dungeon = new Dungeon(
                    id,
                    config.getName(),
                    config.getLevelIds() // Тут List<String>, а не List<DungeonLevel>
            );
            dungeons.put(id, dungeon);
        }

        System.out.println("Загружено уровней: " + levels.size());
        System.out.println("Загружено данжей: " + dungeons.size());



//        DungeonLevel level1 = new DungeonLevel("level_1",50, Arrays.asList("mob_1_1_0","mob_1_1_1"),"boss_1_1",null,"back_1_1");
//        DungeonLevel level2 = new DungeonLevel("level_2",120, Arrays.asList("mob_1_2_0","mob_1_2_1"),"boss_1_2",null,"back_1_2" );
//        DungeonLevel level3 = new DungeonLevel("level_3",250, Arrays.asList("mob_2_1_0","mob_2_1_1","mob_2_1_2"),"boss_2_1",null,"back_1_3" );
//        DungeonLevel level4 = new DungeonLevel("level_4",1000, Arrays.asList("mob_2_2_0","mob_2_2_1","mob_2_2_2","mob_2_2_3"),"boss_2_2",null,"back_1_4" );
//
//        DungeonLevel level5 = new DungeonLevel("level_5",4000, Arrays.asList("mob_2_3_0","mob_2_3_1","mob_2_3_2"),"boss_2_3",null,"back_1_5" );
//        DungeonLevel level6 = new DungeonLevel("level_6",20000, Arrays.asList("mob_2_4_0","mob_2_4_1","mob_2_4_2","mob_2_4_3"),"boss_2_4",null,"back_1_6" );
//        DungeonLevel level7 = new DungeonLevel("level_7",80000, Arrays.asList("mob_3_1_0","mob_3_1_1","mob_3_1_2"),"boss_3_1",null,"back_1_7" );
//        DungeonLevel level8 = new DungeonLevel("level_8",200000, Arrays.asList("mob_3_2_0","mob_3_2_1","mob_3_2_2"),"boss_3_2",null,"back_1_8" );
//        DungeonLevel level9 = new DungeonLevel("level_9",500000, Arrays.asList("mob_3_3_0","mob_3_3_1","mob_3_3_2"),"boss_3_3",null,"back_1_9" );
//
//
//        dungeons.put("first",new Dungeon("first","Первое подземелье",Arrays.asList("level_1","level_2")));
//        dungeons.put("avali",new Dungeon("avali","Вторжение авалей",Arrays.asList("level_3","level_4","level_5","level_6")));
//        dungeons.put("rept",new Dungeon("rept","Вторжение рептилий",Arrays.asList("level_7","level_8","level_9")));

        /*
        Collection<EnemyMob> mobs1 = Arrays.asList(
                new EnemyMob("mob_1_1_0","ai_doll_0","Pizdarik",10,10,1,1),
                new EnemyMob("mob_1_1_1","ai_doll_1","Pizdarik 2",12,5,1,1)
                );
        DungeonLevel level1 = new DungeonLevel(1,50, mobs1,new Boss("boss_1_1","ai_doll_2","Pizdarik Boss",30,1,10L,10L),null );
        Collection<EnemyMob> mobs2 = Arrays.asList(
                new EnemyMob("mob_1_2_0","doll_0","Pizdarik",20,10,2,1),
                new EnemyMob("mob_1_2_1","doll_1","Pizdarik 2",24,12,3,1)
        );
        DungeonLevel level2 = new DungeonLevel(2,120, mobs2,new Boss("boss_1_2","doll_2","Pizdarik Boss 2",60,1,50L,30L),null,"back_1_2" );
        Collection<EnemyMob> mobs3 = Arrays.asList(
                new EnemyMob("mob_2_1_0","avali_1","Солдат авали",50,20,8,1),
                new EnemyMob("mob_2_1_1","avali_2","Солдат авали",55,22,10,1),
                new EnemyMob("mob_2_1_2","avali_3","Солдат авали",60,25,12,1)
        );
        DungeonLevel level3 = new DungeonLevel(1,250, mobs3,new Boss("boss_2_1","avali_boss_1","Полководец Авал",150,1,250L,30L),null,"back_1_3" );

        Collection<EnemyMob> mobs4 = Arrays.asList(
                new EnemyMob("mob_2_2_0","avali_4","Солдат авали",100,45,25,1),
                new EnemyMob("mob_2_2_1","avali_5","Солдат авали",110,50,28,1),
                new EnemyMob("mob_2_2_2","avali_6","Солдат авали",115,55,30,1),
                new EnemyMob("mob_2_2_3","avali_7","Солдат авали",120,60,33,1)
        );
        DungeonLevel level4 = new DungeonLevel(2,1000, mobs4,new Boss("boss_2_2","avali_boss_2","Генерал Авал ",1000,1,1000L,45L),null,"back_1_4" );
        Collection<EnemyMob> mobs5 = Arrays.asList(
                new EnemyMob("mob_2_3_0","avali_8","Сержант авали",240,100,100,1),
                new EnemyMob("mob_2_3_1","avali_9","Сержант авали",250,110,110,1),
                new EnemyMob("mob_2_3_2","avali_10","Сержант авали",255,115,120,1)
        );
        DungeonLevel level5 = new DungeonLevel(3,4000, mobs5,new Boss("boss_2_3","avali_boss_3","Великий Авал ",5500,1,6000L,45L),null,"back_1_5" );
        Collection<EnemyMob> mobs6 = Arrays.asList(
                new EnemyMob("mob_2_4_0","avali_11","Офицер авали",1000,250,300,1),
                new EnemyMob("mob_2_4_1","avali_12","Офицер авали",1100,260,320,1),
                new EnemyMob("mob_2_4_2","avali_13","Офицер авали",1200,275,330,1),
                new EnemyMob("mob_2_4_3","avali_14","Офицер авали",1250,280,350,1)
        );
        DungeonLevel level6 = new DungeonLevel(4,20000, mobs6,new Boss("boss_2_4","avali_boss_4","Божество Авали",25000,1,20000L,45L),null,"back_1_6" );

        Collection<EnemyMob> mobs7 = Arrays.asList(
                new EnemyMob("mob_3_1_0","lizard_1","Ящерица",5000,700,800,1),
                new EnemyMob("mob_3_1_1","lizard_2","Ящерица",5500,750,900,1),
                new EnemyMob("mob_3_1_2","lizard_3","Ящерица",5900,805,1000,1)
        );
        DungeonLevel level7 = new DungeonLevel(1,80000, mobs7,new Boss("boss_3_1","lizard_boss","Ребенок Дракона",100000,1,100000L,45L),null,"back_1_7" );


        Collection<EnemyMob> mobs8 = Arrays.asList(
                new EnemyMob("mob_3_2_0","snake_1","Змеюка",12000,1500,5000,1),
                new EnemyMob("mob_3_2_1","snake_2","Змеюка",13000,1600,6000,1),
                new EnemyMob("mob_3_2_2","snake_3","Змеюка",14000,1705,7000,1)
        );
        DungeonLevel level8 = new DungeonLevel(2,200000, mobs8,new Boss("boss_3_2","snake_boss","Дракон",400000,1,500000L,45L),null,"back_1_8" );


        Collection<EnemyMob> mobs9 = Arrays.asList(
                new EnemyMob("mob_3_3_0","rept_1","Рептилоид",40000,3000,20000,1),
                new EnemyMob("mob_3_3_1","rept_2","Рептилоид",41000,3200,21000,1),
                new EnemyMob("mob_3_3_2","rept_3","Рептилоид",42000,3505,22000,1)
        );
        DungeonLevel level9 = new DungeonLevel(3,500000, mobs9,new Boss("boss_3_3","rept_boss","Марк Цукерберг",2000000,1,3000000L,45L),null,"back_1_9" );



        dungeons.add(new Dungeon("first","Первое подземелье",Arrays.asList(level1,level2)));
        dungeons.add(new Dungeon("avali","Вторжение авалей",Arrays.asList(level3,level4,level5,level6)));
        dungeons.add(new Dungeon("rept","Вторжение рептилий",Arrays.asList(level7,level8,level9)));
        */


    }

    public static EnemyMob getMob(String id){
        return mobs.get(id);
    }
    public static Boss getBoss(String id){
        return bosses.get(id);
    }

    public static void destroy(){
        dungeons.clear();
    }

    public static Dungeon getDungeon(String id){
        return dungeons.get(id);
    }
    public static Dungeon getFirstDung(){
        return dungeons.get(0);
    }
    public static boolean hasNextDungeon(String id){
        return !dungeons.get(dungeons.size()-1).equalsId(id);
    }

    public static EnemyMob rollMob(Collection<String> ids){
        System.out.println("ROLL MOB");

        System.out.println("USE LIST OF: ");
        Set<RollSlot<EnemyMob>> m = new HashSet<>();
        int all_w = 0;
        for(String id : ids){

            System.out.println(id);
            EnemyMob mob = mobs.get(id);
            m.add(new RollSlot<EnemyMob>(mob, mob.getWeight()));
            all_w+=mob.getWeight();
        }
        double w_point = 1.0/all_w;
        Double random = rand.nextDouble();
        double used = 0.0;
        for(RollSlot<EnemyMob> slot : m){

            slot.setTo(used+slot.getWeight()*w_point);
            System.out.println("ROLL BOUND: "+slot.getTo()+" on "+random);
            if(slot.inBound(random)){
                return slot.getLot().clone();
            }
            used+=slot.getWeight()*w_point;
        }

        System.out.println("ROLL MOB FAIL((");
        return null;
    }
    public static Dungeon getNextDung(String id){
        for(int i = 0;i<dungeons.size();i++){
            if(dungeons.get(i).equalsId(id)){
                if(dungeons.size()>i+1){
                    return dungeons.get(i+1);
                }
            }
        }
        return dungeons.get(0);
    }

    public static int getDungIndex(String id){
        for(int i = 0;i<dungeons.size();i++){
            if(dungeons.get(i).equalsId(id)){
                return i+1;
            }
        }
        return 0;
    }

}
