package Sac.Alpi

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object CreerSacNom : Screen("creer_sac_nom")
    object CreerSacDetail : Screen("creer_sac_detail/{sacId}") {
        fun createRoute(sacId: Long) = "creer_sac_detail/$sacId"
    }
    object MesSacs : Screen("mes_sacs")
    object Categories : Screen("categories")
}
