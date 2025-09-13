package com.automarketplace.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Service für Firestore Operationen
 * Bietet generische CRUD-Operationen für alle Entitäten
 */
@Service
public class FirestoreService {
    
    private static final Logger logger = LoggerFactory.getLogger(FirestoreService.class);
    private final Firestore firestore;
    
    public FirestoreService() {
        this.firestore = FirestoreClient.getFirestore();
    }
    
    /**
     * Dokument erstellen oder aktualisieren
     */
    public <T> String saveDocument(String collection, String documentId, T data) {
        try {
            DocumentReference docRef;
            
            if (documentId != null && !documentId.isEmpty()) {
                docRef = firestore.collection(collection).document(documentId);
            } else {
                docRef = firestore.collection(collection).document();
            }
            
            ApiFuture<WriteResult> result = docRef.set(data);
            WriteResult writeResult = result.get();
            
            logger.info("Dokument gespeichert in {}/{} um {}", 
                       collection, docRef.getId(), writeResult.getUpdateTime());
            
            return docRef.getId();
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fehler beim Speichern des Dokuments in {}: {}", collection, e.getMessage());
            throw new RuntimeException("Fehler beim Speichern des Dokuments", e);
        }
    }
    
    /**
     * Dokument nach ID abrufen
     */
    public <T> T getDocument(String collection, String documentId, Class<T> clazz) {
        try {
            DocumentReference docRef = firestore.collection(collection).document(documentId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            
            if (document.exists()) {
                T result = document.toObject(clazz);
                logger.info("Dokument gefunden: {}/{}", collection, documentId);
                return result;
            } else {
                logger.warn("Dokument nicht gefunden: {}/{}", collection, documentId);
                return null;
            }
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fehler beim Abrufen des Dokuments {}/{}: {}", 
                        collection, documentId, e.getMessage());
            throw new RuntimeException("Fehler beim Abrufen des Dokuments", e);
        }
    }
    
    /**
     * Alle Dokumente einer Collection abrufen
     */
    public <T> List<T> getAllDocuments(String collection, Class<T> clazz) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collection).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            List<T> results = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                T item = document.toObject(clazz);
                results.add(item);
            }
            
            logger.info("Gefunden {} Dokumente in Collection {}", results.size(), collection);
            return results;
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fehler beim Abrufen aller Dokumente aus {}: {}", collection, e.getMessage());
            throw new RuntimeException("Fehler beim Abrufen der Dokumente", e);
        }
    }
    
    /**
     * Dokumente mit Query abrufen
     */
    public <T> List<T> queryDocuments(String collection, String field, Object value, Class<T> clazz) {
        try {
            Query query = firestore.collection(collection).whereEqualTo(field, value);
            ApiFuture<QuerySnapshot> future = query.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            List<T> results = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                T item = document.toObject(clazz);
                results.add(item);
            }
            
            logger.info("Query {} = {} ergab {} Ergebnisse in {}", 
                       field, value, results.size(), collection);
            return results;
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fehler bei Query in {}: {}", collection, e.getMessage());
            throw new RuntimeException("Fehler bei der Abfrage", e);
        }
    }
    
    /**
     * Dokument löschen
     */
    public void deleteDocument(String collection, String documentId) {
        try {
            ApiFuture<WriteResult> writeResult = firestore.collection(collection)
                .document(documentId).delete();
            
            writeResult.get();
            logger.info("Dokument gelöscht: {}/{}", collection, documentId);
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fehler beim Löschen des Dokuments {}/{}: {}", 
                        collection, documentId, e.getMessage());
            throw new RuntimeException("Fehler beim Löschen des Dokuments", e);
        }
    }
    
    /**
     * Dokument aktualisieren (nur bestimmte Felder)
     */
    public void updateDocument(String collection, String documentId, Map<String, Object> updates) {
        try {
            DocumentReference docRef = firestore.collection(collection).document(documentId);
            ApiFuture<WriteResult> writeResult = docRef.update(updates);
            
            writeResult.get();
            logger.info("Dokument aktualisiert: {}/{}", collection, documentId);
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fehler beim Aktualisieren des Dokuments {}/{}: {}", 
                        collection, documentId, e.getMessage());
            throw new RuntimeException("Fehler beim Aktualisieren des Dokuments", e);
        }
    }
    
    /**
     * Prüfen ob Dokument existiert
     */
    public boolean documentExists(String collection, String documentId) {
        try {
            DocumentReference docRef = firestore.collection(collection).document(documentId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            
            return document.exists();
            
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fehler beim Prüfen der Existenz von {}/{}: {}", 
                        collection, documentId, e.getMessage());
            return false;
        }
    }
}
