package com.example.runwater

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.example.runwater.ui.theme.RunWaterTheme


class MainActivity : ComponentActivity() {
    private var apps : MutableList<ResolveInfo> =  ArrayList()
    private var arrayApp: MutableList<AppInfoBean> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        //WindowCompat.setDecorFitsSystemWindows(window, false)
//        val systemUiController = rememberSystemUiController()
//        val useDarkIcons = MaterialTheme.colors.isLight

        setContent {

            RunWaterTheme {
                AppList(arrayApp)
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
//
//                    AppList(arrayApp)
//                }
            }
        }
    }

    //获取已安装app列表
    private fun initData(){
        apps = scanApps()
        val pm = packageManager
        Log.d("testMain num of apps",apps.size.toString())
        for (i in apps){
            Log.d("testMain",i.loadLabel(pm).toString())
          //  if (BuildConfig.APPLICATION_ID == i.activityInfo.packageName) continue  //过滤掉 自己本身
            val appinfo = AppInfoBean()
            appinfo.activityName = i.activityInfo.name // 获得该应用程序的启动Activity的name
            appinfo.packageName = i.activityInfo.packageName // 获得应用程序的包名
            appinfo.appLabel = i.loadLabel(pm).toString() // 获得应用程序的Label
            appinfo.icon = i.loadIcon(packageManager) // 获得应用程序图标
            // 为应用程序的启动Activity 准备Intent
            appinfo.intent = Intent()
            appinfo.intent!!.component = ComponentName(i.activityInfo.packageName, i.activityInfo.name)
            arrayApp.add(appinfo)
        }
    }
    private fun scanApps(): MutableList<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN ,null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return packageManager.queryIntentActivities(intent,0)
    }



}

@Composable
fun AppList(arrayApp: MutableList<AppInfoBean>){
    Row(
        Modifier.fillMaxWidth(),
        //horizontalArrangement = Arrangement.Center,//设置水平居中对齐
        //verticalAlignment =  Alignment.CenterVertically//设置垂直居中对
    ) {
        LazyColumn(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(arrayApp.size) { index ->
                val info = arrayApp[index]
                // 列表单项的UI
                TextButtonDemo(info)
            }
        }
    }


}

@Composable
fun TextButtonDemo(appInfoBean: AppInfoBean) {
    val context = LocalContext.current

//    AsyncImage(
//        model = appInfoBean.icon,
//        contentDescription = null
//    )

    TextButton(onClick = {
        context.startActivity(appInfoBean.intent);
    }) {
        Text(
            appInfoBean.appLabel.toString(),
            color = Color.White)
        Box(
            modifier = Modifier
                .size(10.dp)
                .padding(1.dp),
            contentAlignment = Alignment.Center
        ) {
        }
    }
}




class AppInfoBean {
    var intent: Intent? = null
    var activityName: String? = null
    var packageName: String? = null
    var appLabel: String? = null
    var icon: Drawable? = null
}
