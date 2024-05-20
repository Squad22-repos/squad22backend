package com.example.squad22backend.services;

import com.example.squad22backend.repositories.CommercialRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class CommercialServices {

    private final CommercialRepository commercialRepository;

    public CommercialServices(CommercialRepository commercialRepository) {
        this.commercialRepository = commercialRepository;
    }

    public void updateCommercialUserData(String id, String[] stores, String[] services, LocalTime opening, LocalTime closing) {
        if (stores != null && stores.length > 0) {
            this.commercialRepository.updateCommercialStores(id, stores);
            this.commercialRepository.updateCommercialStoresTable(id, stores);
        }
        if (services != null && services.length > 0) {
            this.commercialRepository.updateCommercialServices(id, services);
            this.commercialRepository.updateCommercialServicesTable(id, services);
        }
        if (opening != null) {
            this.commercialRepository.updateCommercialOpeningTime(id, opening);
        }
        if (closing != null) {
            this.commercialRepository.updateCommercialClosingTime(id, closing);
        }
    }
}
