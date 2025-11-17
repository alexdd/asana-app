package com.asana.timer.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AsanaRepositoryTest {

    private lateinit var context: Context
    private lateinit var repository: AsanaRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        // Lösche DataStore-Datei für sauberen Test
        clearDataStore()
        repository = AsanaRepository(context)
    }

    @After
    fun tearDown() {
        // Lösche DataStore nach dem Test
        clearDataStore()
    }

    private fun clearDataStore() {
        try {
            val dataStoreDir = File(context.filesDir, "datastore")
            if (dataStoreDir.exists()) {
                dataStoreDir.listFiles()?.forEach { it.delete() }
                dataStoreDir.delete()
            }
            // Warte kurz, damit Dateisystem-Operationen abgeschlossen sind
            Thread.sleep(100)
        } catch (e: Exception) {
            // Ignoriere Fehler beim Löschen
        }
    }

    @Test
    fun testLoadSequencesFromXml_containsDescriptions() = runBlocking {
        // Warte kurz, damit ensureDefaults() ausgeführt wird
        Thread.sleep(500)
        
        val sequences = repository.sequences.first()
        
        // Prüfe, dass Sequenzen geladen wurden
        assertTrue("Es sollten Sequenzen geladen werden", sequences.isNotEmpty())
        
        // Finde die "Mittelstufe klassisch" Sequenz
        val mittelstufeSequence = sequences.find { it.name == "Mittelstufe klassisch" }
        assertNotNull("Die Sequenz 'Mittelstufe klassisch' sollte vorhanden sein", mittelstufeSequence)
        
        val sequence = mittelstufeSequence!!
        assertTrue("Die Sequenz sollte Asanas enthalten", sequence.asanas.isNotEmpty())
        
        // Prüfe erste Asana mit Beschreibung
        val ersteAsana = sequence.asanas.first()
        assertEquals(
            "Die erste Asana sollte 'Anfangsentspannung - Śavāsana' heißen",
            "Anfangsentspannung - Śavāsana",
            ersteAsana.title
        )
        assertEquals(
            "Die erste Asana sollte 260 Sekunden dauern",
            260,
            ersteAsana.durationSeconds
        )
        assertTrue(
            "Die erste Asana sollte eine Beschreibung haben",
            ersteAsana.description.isNotEmpty()
        )
        assertEquals(
            "Die Beschreibung sollte korrekt sein",
            "Ankommen, Bauchatmung, Progressive Muskelentspannung, Affirmation",
            ersteAsana.description
        )
        
        // Prüfe zweite Asana mit Beschreibung
        if (sequence.asanas.size > 1) {
            val zweiteAsana = sequence.asanas[1]
            assertEquals(
                "Die zweite Asana sollte 'Oṃ' heißen",
                "Oṃ",
                zweiteAsana.title
            )
            assertTrue(
                "Die zweite Asana sollte eine Beschreibung haben",
                zweiteAsana.description.isNotEmpty()
            )
            assertEquals(
                "Die Beschreibung sollte korrekt sein",
                "Oṃ Śānti und Frieden",
                zweiteAsana.description
            )
        }
        
        // Prüfe, dass alle Asanas eine description-Eigenschaft haben (auch wenn leer)
        sequence.asanas.forEach { asana ->
            assertNotNull("Jede Asana sollte eine description-Eigenschaft haben", asana.description)
        }
    }

    @Test
    fun testLoadSequencesFromXml_totalDurationIs90Minutes() = runBlocking {
        // Warte kurz, damit ensureDefaults() ausgeführt wird
        Thread.sleep(500)
        
        val sequences = repository.sequences.first()
        val mittelstufeSequence = sequences.find { it.name == "Mittelstufe klassisch" }
        
        assertNotNull("Die Sequenz 'Mittelstufe klassisch' sollte vorhanden sein", mittelstufeSequence)
        
        val totalSeconds = mittelstufeSequence!!.asanas.sumOf { it.durationSeconds }
        val expectedSeconds = 90 * 60 // 90 Minuten
        
        assertEquals(
            "Die Gesamtdauer sollte genau 90 Minuten (5400 Sekunden) betragen",
            expectedSeconds,
            totalSeconds
        )
    }

    @Test
    fun testLoadSequencesFromXml_allAsanasHaveDescriptionField() = runBlocking {
        // Warte kurz, damit ensureDefaults() ausgeführt wird
        Thread.sleep(500)
        
        val sequences = repository.sequences.first()
        val mittelstufeSequence = sequences.find { it.name == "Mittelstufe klassisch" }
        
        assertNotNull("Die Sequenz 'Mittelstufe klassisch' sollte vorhanden sein", mittelstufeSequence)
        
        // Prüfe, dass alle Asanas eine description-Eigenschaft haben
        mittelstufeSequence!!.asanas.forEachIndexed { index, asana ->
            assertNotNull(
                "Asana #$index (${asana.title}) sollte eine description-Eigenschaft haben",
                asana.description
            )
        }
        
        // Prüfe spezifische Asanas mit bekannten Beschreibungen
        val asanaWithDescription = mittelstufeSequence.asanas.find { 
            it.title.contains("Kapālabhāti") 
        }
        if (asanaWithDescription != null) {
            assertTrue(
                "Kapālabhāti sollte eine Beschreibung haben",
                asanaWithDescription.description.isNotEmpty()
            )
        }
    }
}

