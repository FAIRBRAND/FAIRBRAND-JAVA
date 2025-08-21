package ca.coltip.data.repository;

import ca.coltip.data.entities.AppointmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, String> {}
