package ca.coltip.repository;

import ca.coltip.data.dto.SlotDto;
import ca.coltip.data.entity.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.stream.Stream;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
  @Query(
    """
    SELECT new ca.coltip.data.dto.SlotDto(st, :timezone) FROM Slot st
    INNER JOIN st.user
    WHERE date(st.startAt) >= :start AND date(st.endAt) <= :end
    ORDER BY st.startAt ASC
    """
  )
  Page<SlotDto> findAllInRange(
    @Param("start") LocalDate start,
    @Param("end") LocalDate end,
    @Param("timezone") String timezone,
    Pageable pageable
  );

  @Query(
    """
    SELECT st FROM Slot st
    INNER JOIN st.user
    WHERE date(st.startAt) >= :start AND date(st.endAt) <= :end
    ORDER BY st.startAt ASC
    """
  )
  Stream<Slot> findAllInRange(
    @Param("start") LocalDate start,
    @Param("end") LocalDate end
  );
}
