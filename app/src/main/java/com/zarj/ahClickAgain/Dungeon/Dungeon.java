package com.zarj.ahClickAgain.Dungeon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Dungeon implements Serializable {
    private static final long serialVersionUID = 11L;
    List<String> levelIds = new ArrayList<>();
    String name;
    String id;


    public Dungeon(String id, String name, Collection<String> newLevels){
        this.id = id;
        this.name = name;
        this.levelIds.clear();

        this.levelIds.addAll(newLevels);
    }
    public boolean equalsId(String id){
        return this.id.equals(id);
    }

    public DungeonLevel getLevelOrFirst(String id){
        int currentIndex = levelIds.indexOf(id);
        if (currentIndex == -1) {
            return DungeonManager.levels.get(levelIds.get(0));
        }else{
            String nextId = levelIds.get(currentIndex);
            return DungeonManager.levels.get(nextId);
        }
    }

    public DungeonLevel getNextLevel(String id){
        int currentIndex = levelIds.indexOf(id);
        if(levelIds.size()>currentIndex+1){
            return DungeonManager.levels.get(levelIds.get(currentIndex+1)) ;
        }
        return DungeonManager.levels.get(levelIds.get(0));
    }
    public DungeonLevel getFirstLevel(){
        return DungeonManager.levels.get(levelIds.get(0));
    }
    public String getName(){
        return name;
    }

    public  int levelIndexPlus(String  id){
        for(int i = 0; i< levelIds.size(); i++){
            if(DungeonManager.levels.get(i).checkId(id)){
                return i+1;
            }
        }
        return 1;
    }
    public boolean isLastLevel(String id){
        return DungeonManager.levels.get(levelIds.size()-1).checkId(id);
    }



    public int getDLevelIndex(String id){
        for(int i = 0; i< levelIds.size(); i++){
            if(DungeonManager.levels.get(i).checkId(id)){
                return i+1;
            }
        }
        return 0;
    }
    public String getId(){
        return id;
    }

}
