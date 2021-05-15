package sam.frampton.parcferme

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.databinding.ActivityMainBinding
import sam.frampton.parcferme.viewmodels.MainActivityViewModel
import sam.frampton.parcferme.viewmodels.SeasonViewModel

class MainActivity : AppCompatActivity() {

    private val seasonViewModel: SeasonViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupObservers()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.driverListFragment,
                R.id.constructorListFragment,
                R.id.raceListFragment,
                R.id.standingListFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupObservers() {
        seasonViewModel.refreshResult.observe(this) { refreshResult ->
            refreshResult?.let {
                showError(refreshResult)
                seasonViewModel.clearRefreshResult()
            }
        }
        mainActivityViewModel.refreshResult.observe(this) { refreshResult ->
            refreshResult?.let {
                showError(refreshResult)
                mainActivityViewModel.clearRefreshResult()
            }
        }
    }

    private fun showError(refreshResult: RefreshResult) {
        val text =
            when (refreshResult) {
                RefreshResult.NETWORK_ERROR -> R.string.error_message_network
                RefreshResult.OTHER_ERROR -> R.string.error_message_other
                else -> return
            }
        Snackbar.make(binding.root, text, BaseTransientBottomBar.LENGTH_LONG)
            .apply { anchorView = binding.bottomNavigation }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                mainActivityViewModel.setMenuRefresh()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}