package com.restropos.systemorder.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.restropos.systemorder.dto.OrderDto;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    public void save(OrderDto orderDto){
        Firestore firestore = FirestoreClient.getFirestore();
        firestore
                .collection(orderDto.getWorkspaceTableDto().getWorkspaceDto().getBusinessDomain())
                .document(orderDto.getId())
                .set(orderDto);
    }

    public void delete(OrderDto orderDto){
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(orderDto.getWorkspaceTableDto().getWorkspaceDto().getBusinessDomain())
                .document(orderDto.getId())
                .delete();
    }


    }
