package com.zarj.ahClickAgain

import android.app.Application
import android.content.SharedPreferences
import com.zarj.ahClickAgain.Dungeon.DungeonManager
import com.zarj.ahClickAgain.Dungeon.enemys.Boss
import com.zarj.ahClickAgain.data.SaveManager
import com.zarj.ahClickAgain.items.ItemsManager
import com.zarj.ahClickAgain.items.effects.EffectsManager
import com.zarj.ahClickAgain.market.Market
import com.zarj.ahClickAgain.utils.formatNumb
import java.util.Timer
import kotlin.concurrent.timerTask

class ZApp : Application() {
    lateinit var sharedPreferences:SharedPreferences
    companion object huy{
        lateinit var inst:ZApp
        fun getInstance():ZApp{
            return inst
        }
        val ztime = Timer()
    }
    var her = 5;
    override fun onCreate() {
        super.onCreate()
        inst = this
        Datar.load();
        SaveManager.init(applicationContext)
        EffectsManager.load()
        ItemsManager.load()
        sharedPreferences = this.getSharedPreferences("SHKOTEL", MODE_PRIVATE)
        StaticMetrics.updateMectrics()
        DungeonManager.create(applicationContext)
        WeaponManager.create()
        Market.create()
        PetManager.create(sharedPreferences)
        BonusManager.create(sharedPreferences)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        Player.loadFromSharedPer(sharedPreferences)


        ztime.schedule(timerTask {letsDamage(Player.dps)},1000L,1000L)




        her++;
        System.out.println("ЗАПУСК ПРИЛОЖЕНИЯ ТЕЕЕЕЕСТ! ХУЙ = "+her)
    }
    fun letsDamage(damage : Long){
        if(Player.enemy.damage(damage)==0L){
            Player.addCoins(Player.enemy.coins)
            if(Player.enemy is Boss){
                val lvl = Player.on_level.nextLevel
                if(lvl!=null){
                    Player.on_level = lvl;
                }
                MainActivity.ztimer.stop();
            }else{
                Player.on_level.addExp(Player.enemy.exp)
            }
            Player.enemy = Player.on_level.rollEnemy()
            Player.saveInSharedPer(sharedPreferences.edit())
        }
        MainActivity.coins_text.value = "${formatNumb(Player.coins)}"
        MainActivity.text.value = "${Player.enemy.name} ${formatNumb(Player.enemy.hp)}"
    }


}