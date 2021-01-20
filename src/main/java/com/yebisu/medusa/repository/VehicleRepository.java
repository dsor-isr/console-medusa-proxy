package com.yebisu.medusa.repository;

import com.yebisu.medusa.domain.VehicleConfiguration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends ReactiveMongoRepository<VehicleConfiguration, String> {


}
