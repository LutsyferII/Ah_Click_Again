package com.zarj.ahClickAgain

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zarj.ahClickAgain.market.Market
import com.zarj.ahClickAgain.market.MarketSlot
import com.zarj.ahClickAgain.utils.formatNumb

class Shop {
}


@Composable
fun OpenBonus(sp : SharedPreferences, text: MutableState<String>){

    val is_shop = remember { mutableStateOf(true) }
    Spacer(modifier = Modifier.height(200.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .background(color = Color.Gray)


    ){
        Button(onClick = {
            is_shop.value = true
        },
        enabled = !is_shop.value

        ){
            Text("shop")
        }
        Button(onClick = {
            is_shop.value = false
        },
            enabled = is_shop.value

        ){
            Text("unique")
        }
    }

    val reb_coins = remember{mutableStateOf(Player.reb_coins)}


    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Image(
            painter = painterResource(id = R.drawable.rama_2),null, contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(is_shop.value){
                val expand_start = remember{ mutableStateOf(false)}
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp))
                {
                    Image(painter= painterResource(id = R.drawable.rama_3),null, contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxWidth())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(){
                            Image(
                                painter = painterResource(id = R.drawable.reb_coin),
                                contentDescription = "Image",
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp)
                            )
                            Text("${Player.reb_coins}",color = Color.White)
                        }
                        if(!Player.on_level.dungID.id.equals("first")){
                            Button(onClick={
                                expand_start.value = true
                            },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .width(100.dp)
                            ){
                                Text("Переродиться!", fontSize = 13.sp)
                            }
                        }
                    }
                }

                if(expand_start.value){
                    AlertDialog(
                        onDismissRequest = { expand_start.value = false },
                        title = {
                            Text(text = "Перерождение")
                        },
                        text = {
                            Text("Вы уверены, что хотите переродиться? Вы потеряете весь прогресс, но получите ${Player.getHowManyGetOnRebirth()} кристаллов бездны.")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    expand_start.value = false
                                    Player.letsRebirth()
                                    text.value = "${Player.enemy.name} ${Player.enemy.hp}"
                                    Player.saveInSharedPer(sp.edit())
                                }
                            ) {
                                Text("Да!")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    expand_start.value = false
                                }
                            ) {
                                Text("Нет!")
                            }
                        }
                    )
                }

            }

            Column(
                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .fillMaxWidth()
            ){
                val sale :List<Bonus> =  BonusManager.getSalable()
                val static :List<Bonus> = BonusManager.getNotSalable()

                if(is_shop.value){
                    var rows = sale.size/3
                    if(sale.size%3!=0){
                        rows++
                    }
                    var times = 0
                    for(i in 1..rows){
                        Row(horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier.fillMaxWidth()){
                            for(i in 1..3){
                                if(sale.size>times){
                                    BonusSaleSlot(sale.get(times))
                                }
                                times++
                            }
                        }
                        Spacer(Modifier.height(15.dp))
                    }
                }else{
                    var rows = static.size/3
                    if(static.size%3!=0){
                        rows++
                    }
                    var times = 0
                    for(i in 1..rows){
                        Row(horizontalArrangement = Arrangement.SpaceAround,modifier = Modifier.fillMaxWidth()){
                            for(i in 1..3){
                                if(static.size>times){
                                    val bonus = static.get(times)
                                    BonusSaleSlot(bonus)
                                }
                                times++
                            }
                        }
                    }
                }
            }
        }


    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun infoAlert(bonus : Bonus,has:MutableState<Boolean>){
    AlertDialog(
        onDismissRequest = {
            has.value=false
        }
    ){
            Image(painter = painterResource(id = R.drawable.rama_1), contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier
                .fillMaxWidth()
                .height(400.dp))

            Column(
                modifier = Modifier
                    .height(400.dp)
                    .padding(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                if(bonus.sale){
                    val img = LocalContext.current.resources.getIdentifier(bonus.img_id, "drawable", LocalContext.current.packageName)
                    Image(painter = painterResource(id = img),
                        contentDescription = "Image",
                        Modifier.size(100.dp, 100.dp)
                    )
                    Text("${bonus.name}", fontSize = 20.sp)
                    Spacer(Modifier.height(15.dp))
                    Text("${bonus.lore}", fontSize = 13.sp, lineHeight =13.sp)
                }else{
                    Text("${bonus.name}", fontSize = 30.sp)
                    Spacer(Modifier.height(20.dp))
                    Text("${bonus.info}")
                }
            }


    }
}
@Composable
fun BonusSaleSlot(bonus : Bonus){
    val img = LocalContext.current.resources.getIdentifier(bonus.img_id, "drawable", LocalContext.current.packageName)
    val info = remember{mutableStateOf(false)}
    if(info.value){
        infoAlert(bonus = bonus, has = info)
    }
    Box(Modifier
        .size(120.dp, 200.dp), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.rama_1), contentDescription = null, contentScale = ContentScale.FillBounds)
        Column(
            Modifier
                .size(120.dp, 200.dp)
                .clickable {
                    info.value = true
                }
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,



            ){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Image(painter = painterResource(id = img),
                    contentDescription = "Image",
                    Modifier.size(50.dp, 50.dp)
                )
                Text("${bonus.name}", fontSize = 13.sp, lineHeight = 13.sp)
                Text("${bonus.lore}", fontSize = 8.sp, lineHeight = 8.sp)
            }

            if(bonus.sale&&!bonus.has){
                Button(onClick = {
                    if(!bonus.has&&Player.reb_coins>=bonus.price){
                        bonus.has = true
                        Player.reb_coins-=bonus.price
                        bonus.someAction()
                        if(bonus.type.equals(BonusActionType.DAMAGE)||bonus.type.equals(BonusActionType.PERC_DAMAGE)){
                            Player.updateDamage()
                        }else if(bonus.type.equals(BonusActionType.DPS)||bonus.type.equals(BonusActionType.PERC_DPS)){
                            Player.updateDPS()
                        }
                    }
                },
                    modifier = Modifier
                        .width(80.dp)
                        .height(30.dp),
                    enabled = Player.reb_coins>=bonus.price,
                    contentPadding = PaddingValues(all = 0.dp),
                    colors = buttonColors(disabledContainerColor = Color.Gray, disabledContentColor = Color.Black, contentColor = Color.Green)
                ){
                    val price :String = formatNumb(bonus.price)
                    Text("${price}")
                }
            }else if(!bonus.sale){

                if(bonus.has){
                    Text("has",Modifier.background(Color.Gray))
                }else{

                }
            }
        }
    }

}



@Composable
fun OpenPetShop(sp : SharedPreferences, text: MutableState<String>){


    Spacer(modifier = Modifier.height(200.dp))

    Box(){
        Image(
            painter = painterResource(id = R.drawable.rama_5),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.sword),
                "Image",
                modifier = Modifier.size(size = 20.dp)
            )
            Text("${Player.dps}", fontSize = 20.sp)
        }
    }
    Box(modifier = Modifier
        .fillMaxWidth()){
        Image(
            painter = painterResource(id = R.drawable.rama_6),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
            )

        Column(
            modifier = Modifier
                .verticalScroll(
                    rememberScrollState()
                )
                .fillMaxWidth()
        ){

            for(pet in PetManager.pets){
                System.out.println("TEST ")
                PetSlot(sp, pet)
            }

        }
    }
   

}

@Composable
fun PetSlot(sp : SharedPreferences, pet : Pet){
    val img = LocalContext.current.resources.getIdentifier(pet.img_id, "drawable", LocalContext.current.packageName)
    val has = remember{ mutableStateOf(PetManager.hasPet(pet.id))}
    val has_money = remember{mutableStateOf(pet.price<=Player.coins)}

    val is_max = remember{ mutableStateOf(pet.level>=pet.max_level)}


    val color : Color
    if(has.value){
        has_money.value = pet.upgradePrice<=Player.coins&&!is_max.value
        color = Color.Green
    }else if(has_money.value) {
        color = Color.White
    }else{
        color = Color.Gray
    }
    val price = remember {
        if(has.value){
            mutableStateOf(pet.upgradePrice)
        }else{
            mutableStateOf(pet.price)
        }
    }


    val coin_text : String = formatNumb(price.value)
    val sat : Float
    if(has_money.value){
        sat = 1F
    }else{
        sat = 0.2F
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)

    ){
        Image(painter = painterResource(id = R.drawable.back_shape_1), contentDescription = null,Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
        Row(
            verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                    .fillMaxWidth()
                        .padding(horizontal = 15.dp)
        ){

            Image(
                painter = painterResource(id = img),
                contentDescription = "Image",
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()){
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width((200).dp)) {
                    Text(
                        "${pet.name}",
                        color = color,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Row(

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${pet.level} level",color = Color.Green)
                        Row(){
                            Text("${pet.dps}", fontSize = 15.sp, color = Color.Red)
                            Image(
                                painter = painterResource(id = R.drawable.sword),
                                contentDescription = "Image",
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp)
                            )
                        }

                    }
                }
                Box(modifier = Modifier.width(120.dp)){

                    Button(onClick = {
                        if(has.value&&Player.coins>=price.value){
                            pet.levelUp()
                            Player.coins-=price.value
                            is_max.value = pet.level>=pet.max_level
                            Player.updateDPS()
                            price.value = pet.upgradePrice

                        }else if(!has.value&&Player.coins>=price.value){

                            pet.has = true
                            has.value = true
                            Player.coins-=price.value
                            Player.updateDPS()
                            price.value = pet.upgradePrice



                        }

                    }, enabled = has_money.value, modifier = Modifier.width(120.dp), contentPadding = PaddingValues(0.dp), colors = buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent)){
                        Box(contentAlignment = Alignment.Center,modifier = Modifier.fillMaxSize()){
                            Image(
                                painter = painterResource(id = R.drawable.button_1),
                                contentDescription = "Image",
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(120.dp)
                                ,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.FillWidth,

                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(sat) }),

                                //colorFilter = ColorFilter.tint(red, BlendMode.SrcIn),
                                alpha = 1F

                            )
                            Row(){

                                if(has.value){
                                    Image(painter = painterResource(id = R.drawable.ai_sword_6), contentDescription = null,Modifier.size(18.dp))

                                }
                                if(!is_max.value){
                                    Text("$coin_text",color = Color.Yellow)
                                    Image(painter = painterResource(id = R.drawable.coin), contentDescription = null,Modifier.size(18.dp))

                                }else{
                                    Text("MAX",color = Color.Yellow)
                                }
                            }

                        }
                    }

                }





            }

        }

    }


}


@Composable
fun OpenWeaponShop(sp : SharedPreferences, text: MutableState<String>){
    Spacer(modifier = Modifier.height(200.dp))
    Box(){
        Image(
            painter = painterResource(id = R.drawable.rama_5),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("${Player.damage_buff}", fontSize = 20.sp)
            Image(
                painter = painterResource(id = R.drawable.sword),
                "Image",
                modifier = Modifier.size(size = 20.dp)
            )
            Text("${Player.damage}", fontSize = 20.sp)
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(
                rememberScrollState()
            )
            .fillMaxWidth()
            .background(Color.White)
    ){



        for(slot in Market.getSlots()){
            System.out.println("TEST "+slot.weaponImg)
            ShopSlot(slot,sp)
        }

    }

}


@Composable
fun ShopSlot(slot : MarketSlot, sp : SharedPreferences){
    val sword_img = LocalContext.current.resources.getIdentifier(slot.weaponImg, "drawable", LocalContext.current.packageName)



    val has = remember{ mutableStateOf(Player.hasWeapon(slot.weapon.id)||Player.weapon.damage>(slot.weapon as Weapon).damage) }
    val color : Color
    if(has.value){
        color = Color.Gray
    }else{
        color = Color.LightGray
    }
    val active = remember{ mutableStateOf((!has.value&&slot.price<=Player.coins)) }

    val coins_text : String
    if(slot.price<1000){
        coins_text = "${slot.price}"
    }else if(slot.price<1000000){
        coins_text = "${slot.price/1000}.${slot.price%1000/100}k"
    }else{
        coins_text = "${slot.price/1000000}.${slot.price%1000000/100000}m"

    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ){
        Image(painter = painterResource(id = R.drawable.rama_4), contentDescription = null,contentScale = ContentScale.FillBounds,modifier = Modifier.fillMaxSize())
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,


        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = sword_img),
                    contentDescription = "Image",
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){

                        Text("${slot.weapon.name} ", color = Color.Green)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                        Image(
                            painter = painterResource(id = R.drawable.sword),
                            contentDescription = "Image",
                            modifier = Modifier
                                .height(10.dp)
                                .width(10.dp)
                        )
                        Text("${formatNumb((slot.weapon as Weapon).damage)} ",color = Color.Red)
                    }
                }
            }



            if(Player.weapon.damage<(slot.weapon as Sword).damage){
                Button(onClick ={
                    if(Player.hasEnoughMoney(slot.price)){
                        Player.weapon_has.add(slot.weapon.id)
                        Player.decrCoins(slot.price)
                        has.value = true
                        Player.weapon = slot.weapon as Weapon
                        Player.updateDamage()
                        Player.saveInSharedPer(sp.edit())
                        active.value = false
                    }
                },
                    enabled = active.value,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, disabledContainerColor = color),

                    modifier = Modifier.width(100.dp),
                    contentPadding = PaddingValues(all = 0.dp)
                ){
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                        Image(
                            painter = painterResource(id = R.drawable.coin),
                            contentDescription = "Image",
                            modifier = Modifier
                                .height(10.dp)
                                .width(10.dp)
                        )
                        Text(coins_text,color = Color.Yellow)
                    }
                }
            }



        }
    }



}

