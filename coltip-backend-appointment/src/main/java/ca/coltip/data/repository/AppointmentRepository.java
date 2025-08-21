package ca.coltip.data.repository;

import ca.coltip.data.dto.AppointmentDto;
import ca.coltip.data.entities.Appointment;
import ca.coltip.data.entities.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
  Appointment getAppointmentById(Integer id);

  @Query("SELECT new ca.coltip.data.dto.AppointmentDto(a) FROM Appointment a WHERE a.startAt >= :start AND a.endAt <= :end ORDER BY a.startAt ASC")
  Page<AppointmentDto> findAllByStartAndEndDate(
    @Param("start") LocalDate start,
    @Param("end") LocalDate end,
    Pageable pageable
  );

  @Query("SELECT new ca.coltip.data.dto.AppointmentDto(a) FROM Appointment a WHERE a.status = :status AND a.startAt >= :start AND a.endAt <= :end ORDER BY a.startAt ASC")
  Page<AppointmentDto> findAllByStatusAndStartAndEndDate(
    @Param("status")AppointmentStatus status,
    @Param("start") LocalDate start,
    @Param("end") LocalDate end,
    Pageable pageable
  );
}
