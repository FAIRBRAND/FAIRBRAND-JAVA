package ca.coltip.repository;

import ca.coltip.data.dto.AppointmentRequestDto;
import ca.coltip.data.entity.AppointmentRequest;
import ca.coltip.data.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {
  @Query(
    """
    SELECT new ca.coltip.data.dto.AppointmentRequestDto(ap, :timezone) FROM AppointmentRequest ap
    INNER JOIN ap.user
    WHERE date(ap.startAt) >= :start AND date(ap.endAt) <= :end
    ORDER BY ap.startAt ASC
    """
  )
  Page<AppointmentRequestDto> findAllInRange(
    @Param("start") LocalDate start,
    @Param("end") LocalDate end,
    @Param("timezone") String timezone,
    Pageable pageable
  );

  @Query(
    """
    SELECT new ca.coltip.data.dto.AppointmentRequestDto(ap, :timezone) FROM AppointmentRequest ap
    INNER JOIN ap.user
    WHERE ap.status = :status AND (date(ap.startAt) >= :start AND date(ap.endAt) <= :end)
    ORDER BY ap.startAt ASC
    """
  )
  Page<AppointmentRequestDto> findAllInRangeAndStatus(
    @Param("start") LocalDate start,
    @Param("end") LocalDate end,
    @Param("timezone") String timezone,
    @Param("status") AppointmentStatus status,
    Pageable pageable
  );
}
