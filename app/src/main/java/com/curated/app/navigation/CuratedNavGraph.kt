package com.curated.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.curated.app.core.ui.components.CuratedBottomBar
import com.curated.app.core.ui.components.liquid.liquidBackdropSource
import com.curated.app.core.ui.components.liquid.rememberLiquidBackdrop
import com.curated.app.data.model.MainTab
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import com.curated.app.feature.account.AddressesScreen
import com.curated.app.feature.account.EditProfileScreen
import com.curated.app.feature.account.HelpSupportScreen
import com.curated.app.feature.account.NotificationsScreen
import com.curated.app.feature.account.OrdersScreen
import com.curated.app.feature.account.PaymentMethodsScreen
import com.curated.app.feature.account.SettingsScreen
import com.curated.app.feature.ai.AiPromptHolder
import com.curated.app.feature.ai.AiStylistScreen
import com.curated.app.feature.brand.BrandScreen
import com.curated.app.feature.brand.BrandsScreen
import com.curated.app.feature.cart.CartScreen
import com.curated.app.feature.category.CategoryScreen
import com.curated.app.feature.checkout.CheckoutScreen
import com.curated.app.feature.checkout.OrderPlacedScreen
import com.curated.app.feature.home.HomeScreen
import com.curated.app.feature.look.AllLooksScreen
import com.curated.app.feature.look.CuratedLookScreen
import com.curated.app.feature.outfits.OutfitsScreen
import com.curated.app.feature.pdp.PdpScreen
import com.curated.app.feature.profile.ProfileScreen
import com.curated.app.feature.splash.SplashScreen
import com.curated.app.feature.wishlist.WishlistScreen
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import androidx.compose.ui.platform.LocalContext

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AiPromptEntryPoint {
    fun aiPromptHolder(): AiPromptHolder
}

@Composable
fun CuratedNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current.applicationContext
    val promptHolder = EntryPointAccessors.fromApplication(
        context,
        AiPromptEntryPoint::class.java
    ).aiPromptHolder()

    // Goes to a tab from anywhere: screens stacked above the tabs are closed first.
    val openTab: (MainTab) -> Unit = { tab ->
        navController.navigate(Routes.main(tab.route)) {
            popUpTo(Routes.Main)
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Splash,
        modifier = Modifier.fillMaxSize(),
        enterTransition = {
            fadeIn(tween(280)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(280)
            )
        },
        exitTransition = { fadeOut(tween(220)) },
        popEnterTransition = { fadeIn(tween(220)) },
        popExitTransition = {
            fadeOut(tween(220)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(280)
            )
        }
    ) {
        composable(Routes.Splash) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.main("home")) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.Main,
            arguments = listOf(navArgument("tab") { type = NavType.StringType; defaultValue = "home" }),
            enterTransition = { fadeIn(tween(220)) },
            exitTransition = { fadeOut(tween(180)) }
        ) { entry ->
            val tabArg = entry.arguments?.getString("tab") ?: "home"
            val selected = MainTab.entries.find { it.route == tabArg } ?: MainTab.Home
            val vm: MainScaffoldViewModel = hiltViewModel()
            val wishlistCount by vm.wishlistCount.collectAsStateWithLifecycle()

            MainScaffold(
                selected = selected,
                wishlistBadge = wishlistCount,
                onSelectTab = { tab ->
                    // Every tab is the same "main/{tab}" destination, so a switch is just a
                    // single-top relaunch with a new argument. With popUpTo(main/home) +
                    // saveState/restoreState added, taps on the bar did nothing while the
                    // Home tab was showing (they worked from every other tab).
                    navController.navigate(Routes.main(tab.route)) {
                        launchSingleTop = true
                    }
                }
            ) {
                // The tabs are one destination, so without this the system back button
                // would leave the app from any tab. It now matches the back arrows.
                BackHandler(enabled = selected != MainTab.Home) { openTab(MainTab.Home) }

                when (selected) {
                    MainTab.Home -> HomeScreen(
                        onOpenAi = { prompt ->
                            promptHolder.pendingPrompt = prompt
                            navController.navigate(Routes.main(MainTab.Ai.route)) {
                                launchSingleTop = true
                            }
                        },
                        onOpenNotifications = { navController.navigate(Routes.Notifications) },
                        onOpenCart = { navController.navigate(Routes.Cart) },
                        onOpenAllLooks = { navController.navigate(Routes.Looks) },
                        onOpenCategory = { category ->
                            navController.navigate(Routes.category(category.name))
                        },
                        onOpenLook = { id -> navController.navigate(Routes.look(id)) },
                        onOpenPdp = { id -> navController.navigate(Routes.pdp(id)) },
                        onOpenBrand = { id -> navController.navigate(Routes.brand(id)) },
                        onOpenBrands = { navController.navigate(Routes.Brands) }
                    )

                    MainTab.Ai -> AiStylistScreen(
                        onBack = { openTab(MainTab.Home) },
                        onGenerated = { id -> navController.navigate(Routes.look(id)) }
                    )

                    MainTab.Outfits -> OutfitsScreen(
                        onBack = { openTab(MainTab.Home) },
                        onOpenLook = { id -> navController.navigate(Routes.look(id)) }
                    )

                    MainTab.Wishlist -> WishlistScreen(
                        onBack = { openTab(MainTab.Home) },
                        onOpenLook = { id -> navController.navigate(Routes.look(id)) },
                        onOpenPdp = { id -> navController.navigate(Routes.pdp(id)) }
                    )

                    MainTab.Profile -> ProfileScreen(
                        onBack = { openTab(MainTab.Home) },
                        onEditProfile = { navController.navigate(Routes.EditProfile) },
                        onOpenOrders = { navController.navigate(Routes.Orders) },
                        onOpenWishlist = { openTab(MainTab.Wishlist) },
                        onOpenOutfits = { openTab(MainTab.Outfits) },
                        onOpenAddresses = { navController.navigate(Routes.Addresses) },
                        onOpenPayments = { navController.navigate(Routes.Payments) },
                        onOpenHelp = { navController.navigate(Routes.Help) },
                        onOpenSettings = { navController.navigate(Routes.Settings) }
                    )
                }
            }
        }

        composable(
            route = Routes.Look,
            arguments = listOf(navArgument("outfitId") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("outfitId").orEmpty()
            CuratedLookScreen(
                outfitId = id,
                onBack = { navController.popBackStack() },
                onOpenPdp = { pid -> navController.navigate(Routes.pdp(pid)) },
                onOpenCart = { navController.navigate(Routes.Cart) }
            )
        }

        composable(
            route = Routes.Pdp,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("productId").orEmpty()
            PdpScreen(
                productId = id,
                onBack = { navController.popBackStack() },
                onOpenCart = { navController.navigate(Routes.Cart) }
            )
        }

        composable(Routes.Brands) {
            BrandsScreen(
                onBack = { navController.popBackStack() },
                onOpenBrand = { id -> navController.navigate(Routes.brand(id)) }
            )
        }

        composable(
            route = Routes.Brand,
            arguments = listOf(navArgument("brandId") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("brandId").orEmpty()
            BrandScreen(
                brandId = id,
                onBack = { navController.popBackStack() },
                onOpenPdp = { pid -> navController.navigate(Routes.pdp(pid)) },
                onOpenLook = { oid -> navController.navigate(Routes.look(oid)) },
                onOpenCart = { navController.navigate(Routes.Cart) }
            )
        }

        composable(
            route = Routes.Category,
            arguments = listOf(navArgument(Routes.CategoryArg) { type = NavType.StringType })
        ) {
            // The screen's ViewModel reads the starting category from the route itself.
            CategoryScreen(
                onBack = { navController.popBackStack() },
                onOpenPdp = { id -> navController.navigate(Routes.pdp(id)) }
            )
        }

        composable(Routes.Looks) {
            AllLooksScreen(
                onBack = { navController.popBackStack() },
                onOpenLook = { id -> navController.navigate(Routes.look(id)) }
            )
        }

        composable(Routes.Cart) {
            val vm: MainScaffoldViewModel = hiltViewModel()
            val wishlistCount by vm.wishlistCount.collectAsStateWithLifecycle()
            // The mockup keeps the tab bar on the cart, with no tab lit.
            MainScaffold(
                selected = null,
                wishlistBadge = wishlistCount,
                onSelectTab = openTab
            ) {
                CartScreen(
                    onBack = { navController.popBackStack() },
                    onCheckout = { navController.navigate(Routes.Checkout) },
                    bottomBarSpace = BottomBarSpace
                )
            }
        }

        composable(Routes.Checkout) {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onManageAddresses = { navController.navigate(Routes.Addresses) },
                onManagePayments = { navController.navigate(Routes.Payments) },
                onOrderPlaced = { orderId ->
                    // The cart is empty now, so it and the checkout leave the back stack.
                    navController.navigate(Routes.orderPlaced(orderId)) { popUpTo(Routes.Main) }
                }
            )
        }

        composable(
            route = Routes.OrderPlaced,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { entry ->
            OrderPlacedScreen(
                orderId = entry.arguments?.getString("orderId").orEmpty(),
                onViewOrders = { navController.navigate(Routes.Orders) { popUpTo(Routes.Main) } },
                onContinueShopping = { openTab(MainTab.Home) }
            )
        }

        composable(Routes.Notifications) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.EditProfile) {
            EditProfileScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.Orders) {
            OrdersScreen(
                onBack = { navController.popBackStack() },
                onStartShopping = { openTab(MainTab.Home) }
            )
        }

        composable(Routes.Addresses) {
            AddressesScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.Payments) {
            PaymentMethodsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.Help) {
            HelpSupportScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.Settings) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

/** Height the floating tab bar takes up above the navigation bar. */
private val BottomBarSpace = 80.dp

@Composable
private fun MainScaffold(
    selected: MainTab?,
    wishlistBadge: Int,
    onSelectTab: (MainTab) -> Unit,
    content: @Composable () -> Unit
) {
    // The tab content is what the bar refracts; the bar is its sibling, never its child.
    // Below Android 13 there is no liquid backdrop and the bar falls back to a Haze blur.
    val backdrop = rememberLiquidBackdrop()
    val hazeState = remember { HazeState() }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (backdrop != null) {
                        Modifier.liquidBackdropSource(backdrop)
                    } else {
                        Modifier.hazeSource(state = hazeState)
                    }
                )
        ) {
            content()
        }
        CuratedBottomBar(
            selected = selected,
            wishlistBadge = wishlistBadge,
            onSelect = onSelectTab,
            modifier = Modifier.align(Alignment.BottomCenter),
            hazeState = if (backdrop == null) hazeState else null,
            backdrop = backdrop
        )
    }
}
