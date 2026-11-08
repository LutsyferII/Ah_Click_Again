package com.zarj.ahClickAgain


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zarj.ahClickAgain.Dungeon.enemys.Boss
import com.zarj.ahClickAgain.Dungeon.enemys.Enemy
import com.zarj.ahClickAgain.ui.theme.ZaebaloTheme
import com.zarj.ahClickAgain.items.Inventory
import com.zarj.ahClickAgain.items.ItemStack
import com.zarj.ahClickAgain.items.ItemsManager
import com.zarj.ahClickAgain.items.effects.EffectsManager
import com.zarj.ahClickAgain.items.effects.LiveEffect
import com.zarj.ahClickAgain.utils.TempVals
import com.zarj.ahClickAgain.utils.formatNumb
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask



class MainActivity : ComponentActivity() {

    companion object main_obj{
        lateinit var inst:MainActivity
        fun getInstance():MainActivity{
            return inst
        }
        val ztime = Timer()
        var task: TimerTask = timerTask {  }
        val ztimer = ZTimer(1000,{},{})
        val coins_text = mutableStateOf("${Player.coins}")
        val timer = mutableLongStateOf(Player.on_level.boss.timer)
        val text = mutableStateOf("${Player.enemy.name} ${formatNumb(Player.enemy.hp)}")
    }



    val dps_off = mutableStateOf(true)


    fun MainActivity(){
        inst = this
    }

    override fun onDestroy() {
        super.onDestroy()
        Player.saveInSharedPer(this.getSharedPreferences("SHKOTEL", MODE_PRIVATE).edit())
        ztimer.stop();
        timer.value = 0L;
        Player.setEnemy(Player.on_level.rollEnemy())
    }
    @SuppressLint("UnrememberedMutableState", "UnusedContentLambdaTargetStateParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = this.getSharedPreferences("SHKOTEL", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()



        setContent {
            ZaebaloTheme {
                val open_side = remember{ mutableStateOf(false) }
                val side_type =  remember{ mutableStateOf(SideType.SHOP) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    val draw_back = LocalContext.current.resources.getIdentifier(Player.on_level.backgroundId, "drawable", LocalContext.current.packageName)

                    Image(
                        painter = painterResource(draw_back),
                        contentDescription = "Image",

                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )


                    var dung_text_1 = mutableStateOf("${Player.on_level.dungID.name}")
                    var dung_text_2 = mutableStateOf("${Player.on_level.dungID.levelIndexPlus(Player.on_level.id)}/${Player.on_level.dungID.levels.size} Exp ${formatNumb(Player.on_level.exp)}/${formatNumb(Player.on_level.need_exp ) }")
                    val mob_id : String = Player.enemy.imgSource;
                    val draw = LocalContext.current.resources.getIdentifier(mob_id, "drawable", LocalContext.current.packageName)
                    val boss_button = remember {mutableStateOf(Player.on_level.exp>=Player.on_level.need_exp)}

                    val text_timer = remember { mutableStateOf("")}
                    val is_boss_killed = remember {
                        mutableStateOf(false)
                    }
                    val diamonds = mutableStateOf(Player.diamond)
                    val diamonds_text = mutableStateOf("${diamonds.value}")
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.size(width = 40.dp, height = 200.dp).padding(top = 50.dp)){
                        for(live: LiveEffect in EffectsManager.lives){
                            val d = LocalContext.current.resources.getIdentifier(live.effect.img, "drawable", LocalContext.current.packageName)
                            Box(contentAlignment = Alignment.TopCenter){
                                Image(
                                    painter = painterResource(id = d),
                                    contentDescription = "Image",
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(50.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                )
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(50.dp)
                                ){
                                    Text("${live.live}/${live.effect.limit}", color = Color.Red, fontWeight = FontWeight.Bold)
                                    Text("${live.timeSec}", color = Color.Blue, fontWeight = FontWeight.Bold)
                                }

                            }
                        }
                    }
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()){
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()

                        ){
                            Row(
                                modifier = Modifier
                                    .width(50.dp),
                            ){

                                if(diamonds.value>=1000){
                                    diamonds_text.value = "${diamonds.value/1000}.${diamonds.value%1000/100}k"
                                }
                                //2500 = 2000 + 500
                                Spacer(Modifier.width(1.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.diamond),
                                    contentDescription = "Image",
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                                Text("${diamonds_text.value}", color = Color.Blue)

                            }
                            Row(
                                modifier = Modifier
                                    .width(70.dp),
                                horizontalArrangement = Arrangement.End
                            ){
                                if(Player.coins>=1000){
                                    coins_text.value = formatNumb(Player.coins)
                                }
                                //2500 = 2000 + 500
                                Text("${coins_text.value}", color = Color.Yellow)
                                Image(
                                    painter = painterResource(id = R.drawable.coin),
                                    contentDescription = "Image",
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                )
                            }
                        }
                        Row(){

                            Column(horizontalAlignment = Alignment.CenterHorizontally , modifier = Modifier.fillMaxWidth()){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){

                                    Text(text=dung_text_1.value,fontSize = 30.sp, color = Color.Green)
                                    Text(text=dung_text_2.value,fontSize = 25.sp, color = Color.Green)
                                    Text(text.value,fontSize = 19.sp, color = Color.Red)
                                    if(Player.enemy is Boss){
                                        Text(text_timer.value, fontSize = 15.sp,color = Color.Blue)
                                    }
                                }

                                ztimer.end_tick.value = Player.on_level.boss.timer*1000L;
                                ztimer.onTick ={
                                    if(timer.value<=0){
                                        if(!is_boss_killed.value){

                                            System.out.println("Fight with boss losses ${timer.value}")
                                            Player.enemy = Player.on_level.rollEnemy()
                                            boss_button.value = true
                                        }else{
                                            is_boss_killed.value = false
                                        }
                                        text_timer.value = ""
                                    }else{
                                        timer.value-=1
                                        text_timer.value = "${timer.value}/${Player.on_level.boss.timer}"
                                    }
                                    System.out.println("Scheld time ${timer.value}")
                                }
                                ztimer.onEnd = {
                                    if(!is_boss_killed.value){
                                        System.out.println("Fight with boss losses ${timer.value}")
                                        Player.enemy = Player.on_level.rollEnemy()
                                        boss_button.value = true
                                    }else{
                                        is_boss_killed.value = false
                                    }

                                    text_timer.value = ""
                                }
                                //dps_timer.start()
                                task = timerTask{
                                    letsDamage(Player.dps,text,timer,text_timer,ztimer,editor)
                                }
                                if(false){
                                    ztime.schedule(timerTask{
                                        letsDamage(Player.dps,text,timer,text_timer,ztimer,editor)
                                    }.also { dps_off.value = false },1000L,1000L)
                                }
                                Button(onClick={
                                    letsDamage(Player.damage,text,timer,text_timer,ztimer,editor)
                                },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    contentPadding = PaddingValues(all = 0.dp),
                                    shape = RoundedCornerShape(size = 25.dp)

                                ){
                                    Image(
                                        painter = painterResource(id = draw),
                                        contentDescription = "Image",
                                        modifier = Modifier
                                            .height(300.dp)
                                            .width(300.dp)
                                    )
                                }
                                if(Player.on_level.exp>=Player.on_level.need_exp){
                                    Button(
                                        onClick={
                                            if(Player.enemy is Boss){
                                                ztimer.stop()
                                                timer.value = 0
                                                Player.enemy  = Player.on_level.rollEnemy()
                                            }else{
                                                boss_button.value = false
                                                timer.value=  Player.on_level.boss.timer
                                                Player.enemy = Player.on_level.boss
                                                text_timer.value = "${timer.value}/${Player.on_level.boss.timer}"
                                                System.out.println("Timer = ${timer.value} must in ${Player.on_level.boss.timer} ")
                                                text.value = "${Player.enemy.name} ${formatNumb(Player.enemy.hp)}"
                                                ztimer.start()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent,disabledContainerColor = Color.Transparent,disabledContentColor = Color.Transparent),
                                        modifier = Modifier
                                            .height(60.dp)
                                            .width(200.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ){
                                        Image(
                                            painter = painterResource(id = R.drawable.boss),
                                            contentDescription = "Image",
                                            modifier = Modifier
                                                .height(70.dp)
                                                .width(200.dp),
                                            contentScale = ContentScale.FillBounds
                                        )
                                    }
                                }
                                Button(
                                    onClick = {

                                        Inventory.giveItem(ItemStack(ItemsManager.getAll().get(0), 20))
                                        //EffectsManager.run("dmg_buff_1",10);
                                        /*
                                        if(TempVals.effect.isRunned){
                                            TempVals.effect.stop()
                                        }else{
                                            TempVals.effect.run()
                                            val tmr = object: CountDownTimer(10000, 1000) {
                                                override fun onTick(millisUntilFinished: Long) {
                                                    TempVals.eff_text = "effect ${TempVals.effect.id} time ${TempVals.effect.timeSec()}";
                                                }
                                                override fun onFinish() {
                                                }
                                            }
                                            tmr.start()
                                            TempVals.eff_text = "effect ${TempVals.effect.id} time ${TempVals.effect.timeSec()}";
                                        }
                                        */
                                    },
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(100.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    enabled = true
                                ){
                                    Text(TempVals.eff_text)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                        //LowerPanel(this_context)
                        Box(){
                            Image(
                                painter = painterResource(id = R.drawable.rama_7),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                            Row(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                ,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround

                            ){
                                val draw_swords = LocalContext.current.resources.getIdentifier("three_swords", "drawable", LocalContext.current.packageName)
                                ImageNiz(SideType.SHOP,draw_swords,open_side,side_type)
                                val draw_pet = LocalContext.current.resources.getIdentifier("pet_shop", "drawable", LocalContext.current.packageName)
                                ImageNiz(SideType.PETS,draw_pet,open_side,side_type)
                                val draw_reb = LocalContext.current.resources.getIdentifier("reb_coin", "drawable", LocalContext.current.packageName)
                                ImageNiz(SideType.REB,draw_reb,open_side,side_type)

                                val draw_inv = LocalContext.current.resources.getIdentifier("backpack", "drawable", LocalContext.current.packageName)
                                ImageNiz(SideType.INV,draw_inv,open_side,side_type)


                                val draw_profile = LocalContext.current.resources.getIdentifier("profile", "drawable", LocalContext.current.packageName)
                                ImageNiz(SideType.PROFILE,draw_profile,open_side,side_type)
                            }
                        }
                    }
                    if(open_side.value){
                        OpenSide(side_type.value,sharedPreferences,coins_text)
                    }
                }
            }
        }
    }

}

@Composable
fun ImageNiz(side : SideType, draw : Int, open_side : MutableState<Boolean>, side_type:MutableState<SideType>){
    Image(
        painter = painterResource(id = draw),
        contentDescription = "Image",
        modifier = Modifier
            .height(40.dp)
            .width(40.dp)
            .clickable {
                if (open_side.value) {
                    if (side_type.value.equals(side)) {
                        open_side.value = false
                    } else {
                        side_type.value = side
                    }
                } else {
                    side_type.value = side
                    open_side.value = true
                }
            }
    )
}


fun getReward(enemy:Enemy){
    Player.addCoins(enemy.coins)

}

@Composable
fun OpenSide(side_type : SideType, sharedPreferences : SharedPreferences, text: MutableState<String>){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
            .defaultMinSize(minHeight = 400.dp)


    ){
        if(side_type.equals(SideType.SHOP)){
            OpenWeaponShop(sp = sharedPreferences,text)
        }else if(side_type.equals(SideType.PETS)){
            OpenPetShop(sp = sharedPreferences, text = text)
        }else if(side_type.equals(SideType.REB)){
            OpenBonus(sp = sharedPreferences, text = text)
        }else if(side_type.equals(SideType.INV)){
            OpenInventory(sp = sharedPreferences, text = text)
        }else if(side_type.equals(SideType.PROFILE)){
            Profile()
        }
    }
}

fun letsDamage(damage : Long,text:MutableState<String>,timer : MutableState<Long>,text_timer : MutableState<String>,ztimer : ZTimer, editor : SharedPreferences.Editor){
    if(Player.enemy.damage(damage)==0L){
        Player.enemy.doDrop();
        Player.addCoins(Player.enemy.coins)
        if(Player.enemy is Boss){
            val lvl = Player.on_level.nextLevel
            if(lvl!=null){
                Player.on_level = lvl;
            }
            timer.value = 0
            text_timer.value=""
            ztimer.stop()
        }else{
            Player.on_level.addExp(Player.enemy.exp)
        }
        Player.enemy = Player.on_level.rollEnemy()
        Player.saveInSharedPer(editor)
    }
    text.value = "${Player.enemy.name} ${formatNumb(Player.enemy.hp)}"
}
@Preview(widthDp = 300, heightDp = 600,showBackground = true)
@Composable
fun ButtonSample() {
    var a = 1
    Button(onClick = {a++}){

        Text("Lol", fontSize = 20.sp)
    }

}
@Composable
fun Profile() {
    Spacer(modifier = Modifier.height(200.dp))
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.Gray).padding(horizontal = 5.dp, vertical = 2.dp).defaultMinSize(minWidth = 200.dp, minHeight = 500.dp)
    ) {
        Text("Чистый урон: ${Player.damage}")
        Text("Добавочный урон: ${Player.damage_buff}")
        Text("Добавочный процент урона: ${Player.perc_damage_buff}")
        Text("Чистый дпс: ${Player.dps}")
        Text("Добавочный дпс: ${Player.dps_buff}")
        Text("Добавочный процент дпс: ${Player.perc_dps_buff}")
        Text("Перерождений: ${Player.rebirthes}")
        Text("Плюс золото в процентах: ${Player.coins_buff}")


    }
}

