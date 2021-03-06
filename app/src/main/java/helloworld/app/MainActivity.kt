package helloworld.app

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import helloworld.app.databinding.ActivityMainBinding
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener

class MainActivity : AppCompatActivity() {
    val LOG_TAG = "AppsFlyerOneLinkSimApp"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appsFlyer = AppsFlyerLib.getInstance()
        val eventValues = HashMap<String, Any>()
        eventValues.put(AFInAppEventParameterName.CONTENT_ID, 12345)
        eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, "555")
        eventValues.put(AFInAppEventParameterName.REVENUE, 50)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        binding.cab.setOnClickListener {
            appsFlyer.logEvent(
                applicationContext,
                AFInAppEventType.PURCHASE,
                eventValues,
                object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        Log.d(LOG_TAG, "Purchase event sent successfully")
                    }
                    override fun onError(errorCode: Int, errorDesc: String) {
                        Log.d(LOG_TAG, "Event failed to be sent:\n" +
                                "Error code: " + errorCode + "\n"
                                + "Error description: " + errorDesc)
                    }
                }
            )
        }
        appsFlyer.setDebugLog(true)
        appsFlyer.setMinTimeBetweenSessions(0)
        appsFlyer.init("g3HjiTFQSYkkNWGWDQazxL", null, this)
        appsFlyer.setCustomerUserId("12345");

        appsFlyer.start(this, "g3HjiTFQSYkkNWGWDQazxL", object :
            AppsFlyerRequestListener {
            override fun onSuccess() {
                Log.d(LOG_TAG, "Launch sent successfully!")
            }

            override fun onError(errorCode: Int, errorDesc: String) {
                Log.d(LOG_TAG, "Launch failed to be sent:\n" +
                        "Error code: " + errorCode + "\n"
                        + "Error description: " + errorDesc)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}