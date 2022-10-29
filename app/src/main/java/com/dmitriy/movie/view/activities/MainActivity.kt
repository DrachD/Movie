package com.dmitriy.movie.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.dmitriy.movie.R
import com.dmitriy.movie.databinding.ActivityMainBinding
import com.dmitriy.movie.model.account.entities.Account
import com.dmitriy.movie.repository.RepositorySharedPreferences
import com.dmitriy.movie.util.viewModelCreator
import com.dmitriy.movie.view.tabs.Repositories
import com.dmitriy.movie.view.tabs.TabsFragment
import com.dmitriy.movie.view.tabs.dashboard.DashboardFragment
import com.dmitriy.movie.view.tabs.profile.ProfileFragment
import com.dmitriy.movie.view.tabs.settings.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    private val viewModel by viewModelCreator {
        MainActivityViewModel(Repositories.accountRepository)
    }

    private val topLevelDestinations = setOf(getTabsDestination(), getSignInDestination())

    // fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            // если текущий фракмент является табом или другим фрагментом
            if (f is TabsFragment || f is NavHostFragment) return

            onNavControllerActivated(f.findNavController())
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        setSupportActionBar(binding.toolbar)

        auth = Firebase.auth

        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                if (f is DashboardFragment || f is SettingsFragment || f is ProfileFragment) {
                    //navController = f.findNavController()
                    onNavControllerActivated(f.findNavController())
                    navController = f.findNavController()
                    //navController?.addOnDestinationChangedListener { _, destination, arguments ->
                    //    title = destination.label
                        //supportActionBar?.title = prepareTitle(destination.label, arguments)
                        //supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
                    //}
                }
            }
        }, true)

        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).navController

        //navController = supportFragmentManager.findFragmentById(R.id.tabsContainer)?.findNavController()
        navController.apply {
            RepositorySharedPreferences(context).loadAccountData(object : RepositorySharedPreferences.Callback {
                override fun onLoadAccountData(account: Account) {
                    //prepareRootNavController(isSignIn, this@apply)
                }
            })
            // Проверяем но то, вошел ли пользователь в данный момент
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // мы вошли
                prepareRootNavController(true, this)
            } else {
                prepareRootNavController(false, this)
            }
            onNavControllerActivated(this)

            // Задаем начальный граф (main_graph) и целевая начальная точка (tabsFragment)
//            graph = navInflater.inflate(R.navigation.main_graph).apply {
//                setStartDestination(R.id.tabsFragment)
//            }
            //onNavControllerActivated(this)
        }

//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            title = destination.label
//        }

//        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
//            override fun onFragmentViewCreated(
//                fm: FragmentManager,
//                f: Fragment,
//                v: View,
//                savedInstanceState: Bundle?
//            ) {
//                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
//                if (f is TabsFragment || f is NavHostFragment) return
//                onNavControllerActivated(f.findNavController())
//            }
//        }, true)

        updateAccountUsername()
    }

    fun updateAccountUsername() {
        RepositorySharedPreferences(
            this@MainActivity
        ).loadAccountData(
            object : RepositorySharedPreferences.Callback {
                override fun onLoadAccountData(account: Account) {
                    binding.usernameTextView.text =
                        if (RepositorySharedPreferences(this@MainActivity).loadAccoutSignIn()) {
                            var email = ""
                            RepositorySharedPreferences(this@MainActivity).loadAccountData(
                                object : RepositorySharedPreferences.Callback {
                                    override fun onLoadAccountData(account: Account) {
                                        email = account.email.toString()
                                    }
                                }
                            )
                            email
                        } else {
                            ""
                        }
                }
            }
        )
    }

    // Получение главного NavContoller находящийся в activity_main.xml
    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    fun onLogoutPressed() {
        auth.signOut()
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()
    }

    // При запуске приложения определяет нужны граф для отображения
    // в зависимости от того, зарегестрирован ли пользователь или нет
    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
        graph.setStartDestination(
            if (isSignedIn) {
                getTabsDestination()
            } else {
                getIntroductionDestination()
            }
        )
        navController.graph = graph
    }

    // У нас два контроллера, один отвечающий за смену фрагментов между регистрацией и главной меню
    // И контроллер смены фрагментов в главном меню
    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    // Целевой слушатель необходим для изменения заголовка в Toolbar
    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, arguments ->
        supportActionBar?.title = prepareTitle(destination.label, arguments)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    //supportActionBar?.title = prepareTitle(destination.label, arguments)
        //supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun prepareTitle(label: CharSequence?, arguments: Bundle?): String {

        // code for this method has been copied from Google sources :)

        if (label == null) return ""
        val title = StringBuffer()
        val fillInPattern = Pattern.compile("\\{(.+?)\\}")
        val matcher = fillInPattern.matcher(label)
        while (matcher.find()) {
            val argName = matcher.group(1)
            if (arguments != null && arguments.containsKey(argName)) {
                matcher.appendReplacement(title, "")
                title.append(arguments[argName].toString())
            } else {
                throw IllegalArgumentException(
                    "Could not find $argName in $arguments to fill label $label"
                )
            }
        }
        matcher.appendTail(title)
        return title.toString()
    }

    private fun isSignedIn(): Boolean {
        // Вытягиваем бандл с сохраненными значениями
        val bundle = intent.extras ?: throw IllegalStateException("No required arguments")
        // Вытягиваем все аргументы
        val args = MainActivityArgs.fromBundle(bundle)
        return args.isSignedIn
    }

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getSignInDestination(): Int = R.id.loginFragment

    private fun getIntroductionDestination(): Int = R.id.introductionFragment
}