package com.example.squad22backend.repositories;

import com.example.squad22backend.models.CommercialUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;

public interface CommercialRepository extends JpaRepository<CommercialUser, String> {

    // Adiciona usuário comercial
    @Modifying
    @Query(value = "insert into commercial_user (user_id, stores, services, opening_time, closing_time)" +
            "VALUES (:id, :stores, :services, :opening_time, :closing_time)", nativeQuery = true)
    @Transactional
    void addCommercial(@Param("id") String id, @Param("stores") String[] stores, @Param("services") String[] services,
                      @Param("opening_time") LocalTime opening_time, @Param("closing_time") LocalTime closing_time);

    // Adiciona os dados do usuário comercial a suas respectivas tabelas
    @Modifying
    @Query(value = "insert into commercial_user_stores (commercial_user_user_id, stores) VALUES (:id, :stores)", nativeQuery = true)
    @Transactional
    void addCommercialStores(@Param("id") String id, @Param("stores") String[] stores);

    @Modifying
    @Query(value = "insert into commercial_user_services (commercial_user_user_id, services) VALUES (:id, :services)", nativeQuery = true)
    @Transactional
    void addCommercialServices(@Param("id") String id, @Param("services") String[] services);

    // Atualiza os dados do usuário comercial
    @Modifying
    @Query(value = "update commercial_user set stores = :newStores where user_id = :id", nativeQuery = true)
    @Transactional
    void updateCommercialStores(@Param("id") String id, @Param("newStores") String[] newStores);

    @Modifying
    @Query(value = "update commercial_user_stores set stores = :newStores where commercial_user_user_id = :id", nativeQuery = true)
    @Transactional
    void updateCommercialStoresTable(@Param("id") String id, @Param("newStores") String[] newStores);

    @Modifying
    @Query(value = "update commercial_user set services = :newServices where user_id = :id", nativeQuery = true)
    @Transactional
    void updateCommercialServices(@Param("id") String id, @Param("newServices") String[] newServices);

    @Modifying
    @Query(value = "update commercial_user_services set services = :newServices where commercial_user_user_id = :id", nativeQuery = true)
    @Transactional
    void updateCommercialServicesTable(@Param("id") String id, @Param("newServices") String[] newServices);

    @Modifying
    @Query(value = "update commercial_user set opening_time = :newOpening where user_id = :id", nativeQuery = true)
    @Transactional
    void updateCommercialOpeningTime(@Param("id") String id, @Param("newOpening") LocalTime newOpening);

    @Modifying
    @Query(value = "update commercial_user set closing_time = :newClosing where user_id = :id", nativeQuery = true)
    @Transactional
    void updateCommercialClosingTime(@Param("id") String id, @Param("newClosing") LocalTime newClosing);

}
