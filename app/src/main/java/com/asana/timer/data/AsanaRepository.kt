package com.asana.timer.data

import android.content.Context
import android.content.res.XmlResourceParser
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import java.util.UUID

class AsanaRepository(context: Context) {

    private val appContext = context.applicationContext
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = scope,
        produceFile = { context.preferencesDataStoreFile(DATASTORE_FILE_NAME) }
    )

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        encodeDefaults = true
    }

    private val sequenceListSerializer = ListSerializer(AsanaSequence.serializer())

    val sequences: Flow<List<AsanaSequence>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SEQUENCES_KEY]
                ?.let { storedJson ->
                    runCatching { json.decodeFromString(sequenceListSerializer, storedJson) }
                        .getOrElse { emptyList() }
                }
                ?: emptyList()
        }

    init {
        scope.launch { ensureDefaults() }
    }

    suspend fun upsertSequence(sequence: AsanaSequence) {
        dataStore.edit { preferences ->
            val current = decode(preferences[SEQUENCES_KEY])
            val updated = current.filterNot { it.id == sequence.id } + sequence
            preferences[SEQUENCES_KEY] = json.encodeToString(sequenceListSerializer, updated)
        }
    }

    suspend fun deleteSequence(sequenceId: String) {
        dataStore.edit { preferences ->
            val current = decode(preferences[SEQUENCES_KEY])
            val updated = current.filterNot { it.id == sequenceId }
            preferences[SEQUENCES_KEY] = json.encodeToString(sequenceListSerializer, updated)
        }
    }

    suspend fun replaceAll(sequences: List<AsanaSequence>) {
        dataStore.edit { preferences ->
            preferences[SEQUENCES_KEY] = json.encodeToString(sequenceListSerializer, sequences)
        }
    }

    private suspend fun ensureDefaults() {
        dataStore.edit { preferences ->
            val current = decode(preferences[SEQUENCES_KEY])
            if (current.isEmpty()) {
                val defaultSequences = loadSequencesFromXml() ?: getFallbackSequences()
                preferences[SEQUENCES_KEY] = json.encodeToString(sequenceListSerializer, defaultSequences)
            }
        }
    }

    private fun loadSequencesFromXml(): List<AsanaSequence>? {
        return try {
            val xmlResId = appContext.resources.getIdentifier(
                "default_sequences",
                "xml",
                appContext.packageName
            )
            if (xmlResId == 0) {
                return null
            }

            val parser = appContext.resources.getXml(xmlResId)
            val sequences = mutableListOf<AsanaSequence>()

            var eventType = parser.eventType
            var currentSequence: MutableList<Asana>? = null
            var sequenceName: String? = null
            var asanaTitle: String? = null
            var asanaDuration: Int? = null
            var asanaDescription: String? = null
            var currentElement: String? = null

            while (eventType != XmlResourceParser.END_DOCUMENT) {
                when (eventType) {
                    XmlResourceParser.START_TAG -> {
                        currentElement = parser.name
                        when (parser.name) {
                            "sequence" -> {
                                currentSequence = mutableListOf()
                                sequenceName = null
                            }
                            "asana" -> {
                                asanaTitle = null
                                asanaDuration = null
                                asanaDescription = null
                            }
                        }
                    }
                    XmlResourceParser.TEXT -> {
                        val text = parser.text.trim()
                        when (currentElement) {
                            "name" -> {
                                if (text.isNotEmpty()) sequenceName = text
                            }
                            "title" -> {
                                if (text.isNotEmpty()) asanaTitle = text
                            }
                            "durationSeconds" -> {
                                asanaDuration = text.toIntOrNull()
                            }
                            "description" -> {
                                // Auch leere Beschreibungen werden übernommen
                                asanaDescription = text
                                android.util.Log.d("AsanaRepository", "Description gefunden: '$text' für Asana: $asanaTitle")
                            }
                        }
                    }
                    XmlResourceParser.END_TAG -> {
                        when (parser.name) {
                            "description" -> {
                                // Stelle sicher, dass description gesetzt wird, auch wenn TEXT-Event leer war
                                if (asanaDescription == null) {
                                    asanaDescription = ""
                                    android.util.Log.d("AsanaRepository", "Description END_TAG: description auf leeren String gesetzt")
                                }
                            }
                            "asana" -> {
                                if (asanaTitle != null && asanaDuration != null && currentSequence != null) {
                                    val finalDescription = asanaDescription ?: ""
                                    android.util.Log.d("AsanaRepository", "Asana erstellt: title='$asanaTitle', description='$finalDescription'")
                                    currentSequence.add(
                                        Asana(
                                            title = asanaTitle,
                                            durationSeconds = asanaDuration,
                                            description = finalDescription
                                        )
                                    )
                                }
                            }
                            "sequence" -> {
                                if (sequenceName != null && currentSequence != null && currentSequence.isNotEmpty()) {
                                    sequences.add(
                                        AsanaSequence(
                                            name = sequenceName,
                                            asanas = currentSequence
                                        )
                                    )
                                }
                                currentSequence = null
                                sequenceName = null
                            }
                        }
                        currentElement = null
                    }
                }
                eventType = parser.next()
            }
            parser.close()

            if (sequences.isNotEmpty()) sequences else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFallbackSequences(): List<AsanaSequence> {
        return listOf(
            AsanaSequence(
                name = "Sanfter Morgenflow",
                asanas = listOf(
                    Asana(title = "Cat / Cow", durationSeconds = 60),
                    Asana(title = "Downward Dog", durationSeconds = 90),
                    Asana(title = "Sun Salutation A", durationSeconds = 180),
                    Asana(title = "Seated Forward Fold", durationSeconds = 120)
                )
            ),
            AsanaSequence(
                name = "Abendliche Entspannung",
                asanas = listOf(
                    Asana(title = "Child's Pose", durationSeconds = 120),
                    Asana(title = "Reclined Twist Left", durationSeconds = 90),
                    Asana(title = "Reclined Twist Right", durationSeconds = 90),
                    Asana(title = "Savasana", durationSeconds = 240)
                )
            )
        )
    }

    private fun decode(raw: String?): List<AsanaSequence> =
        raw?.let { json.decodeFromString(sequenceListSerializer, it) } ?: emptyList()

    companion object {
        private const val DATASTORE_FILE_NAME = "asana_timer.preferences_pb"
        private val SEQUENCES_KEY = stringPreferencesKey("asana_sequences")
    }
}

