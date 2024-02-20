package com.irrelevxnce.jblgroundscare.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExportToFirebase {

    private final String basePath;
    public ExportToFirebase(String basePath) {
        this.basePath = basePath;
    }

    public void makeChildReferences(String [] references, String baseChild){
        for (String child : references) {
            FirebaseDatabase.getInstance().getReference(basePath).child(baseChild).child(child).setValue("");
        }
    }


    public void makeChildReferences(String baseChild){
        FirebaseDatabase.getInstance().getReference(basePath).child(baseChild).setValue("");
    }

    public void addChildValue(String referenceTitle, String referenceChild, String valueToAdd) {
        FirebaseDatabase.getInstance().getReference(basePath).child(referenceTitle).child(referenceChild).setValue(valueToAdd);
    }

    public void addChildValue(String referenceTitle, String referenceChild, Object valueToAdd) {
        FirebaseDatabase.getInstance().getReference(basePath).child(referenceTitle).child(referenceChild).setValue(valueToAdd);
    }

    public void removeChild(String referenceTitle) {
        FirebaseDatabase.getInstance().getReference(basePath).child(referenceTitle).removeValue();
    }
}
