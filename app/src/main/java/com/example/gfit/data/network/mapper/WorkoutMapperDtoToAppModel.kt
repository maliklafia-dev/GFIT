package com.example.gfit.data.network.mapper

import com.example.gfit.data.model.workout.WorkoutExercise as UserWorkoutExercise
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object WorkoutMapper {
    fun fromApiResponse(jsonContent: String): List<UserWorkoutExercise> {
        return try {
            Log.d("WorkoutMapper", "🔄 Début du nettoyage et parsing JSON")

            // Nettoyer la réponse
            val cleanJson = jsonContent
                .replace("```json", "") // Supprimer l'en-tête json
                .replace("```", "")     // Supprimer les backticks
                .trim()                 // Supprimer les espaces inutiles

            Log.d("WorkoutMapper", "📝 JSON nettoyé: $cleanJson")

            // Vérifier le format JSON
            if (!cleanJson.startsWith("[")) {
                Log.e("WorkoutMapper", "❌ Format JSON invalide après nettoyage")
                throw Exception("Le format de la réponse n'est pas un tableau JSON valide")
            }

            val exercises = Gson().fromJson(cleanJson, Array<UserWorkoutExercise>::class.java).toList()
            Log.d("WorkoutMapper", "✅ Parsing réussi: ${exercises.size} exercices")
            exercises
        } catch (e: JsonSyntaxException) {
            Log.e("WorkoutMapper", "❌ Erreur de syntaxe JSON: ${e.message}")
            Log.e("WorkoutMapper", "Contenu problématique: $jsonContent")
            throw Exception("Erreur de syntaxe JSON: ${e.message}")
        } catch (e: Exception) {
            Log.e("WorkoutMapper", "❌ Erreur de parsing: ${e.message}")
            Log.e("WorkoutMapper", "Contenu problématique: $jsonContent")
            throw Exception("Erreur lors du parsing de la réponse: ${e.message}")
        }
    }


}



