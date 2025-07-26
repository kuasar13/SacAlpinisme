package Sac.Alpi.di

import android.content.Context
import androidx.room.Room
import Sac.Alpi.data.AppDatabase

object DatabaseModule {
    // Stocke l'instance unique
    private var instance: AppDatabase? = null

    /**
     * Fournit l'instance de la base, en la créant si elle n'existe pas encore.
     * @param context : n'importe quel context Android, idéalement l'Application ou une Activity.
     */
    fun provideDatabase(context: Context): AppDatabase =
        instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "sac-alpi-db"                // nom du fichier de base
            )
                .fallbackToDestructiveMigration() // optionnel : détruit la base si upgrade sans migration
                .build()
            instance = db
            db
        }
}
